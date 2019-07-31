package issac.study.springdemo.core.config.es;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.ScriptedField;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.*;

public  class  MyResultMapper extends DefaultResultMapper {
        private static final String HIGHLIGHT="highlight";
        private final MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext;

        public MyResultMapper(MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext) {
            super(mappingContext);
            this.mappingContext = mappingContext;
        }

        @Override
        public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
            long totalHits = response.getHits().getTotalHits();
            float maxScore = response.getHits().getMaxScore();

            List<T> results = new ArrayList<>();
            for (SearchHit hit : response.getHits()) {
                if (hit != null) {
                    T result = null;
                    if (!StringUtils.isEmpty(hit.getSourceAsString())) {
                        result = mapEntity(hit.getSourceAsString(), clazz);
                    } else {
                        result = mapEntity(hit.getFields().values(), clazz);
                    }

                    Field field = ReflectionUtils.findField(clazz, HIGHLIGHT);
                    if (field!=null){
                        Map<String,String> highlightMap=new HashMap<>();
                        Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                        if (highlightFields!=null){
                            for (Map.Entry<String, HighlightField>entry:highlightFields.entrySet()){
                                highlightMap.put(convertHighlightName(entry.getKey()),entry.getValue().getFragments()[0].string());
                            }
                        }
                        if (!highlightMap.isEmpty()){
                            Object highlight = JSON.parseObject(JSON.toJSONString(highlightMap), field.getType());
                            try {
                                field.setAccessible(true);
                                field.set(result,highlight);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    setPersistentEntityId(result, hit.getId(), clazz);
                    setPersistentEntityVersion(result, hit.getVersion(), clazz);
                    setPersistentEntityScore(result, hit.getScore(), clazz);

                    populateScriptFields(result, hit);
                    results.add(result);
                }
            }

            return new AggregatedPageImpl<T>(results, pageable, totalHits, response.getAggregations(), response.getScrollId(),
                    maxScore);
        }

        private static String convertToSetMethodName(String origin){
            return  "set"+StringUtils.capitalize(origin);
        }
        private static String convertHighlightName(String origin) {
            String result=convertName(origin,"\\.");
            result=convertName(result,"_");
            return result;
        }
        private static  String convertName(String origin,String delimiter){
            String[] splits = origin.split(delimiter);
            String result=splits[0];
            if (splits.length>1){
                for (int i=1;i<splits.length;i++){
                    result+=StringUtils.capitalize(splits[i]);
                }
            }
            return result;
        }


        private <T> void populateScriptFields(T result, SearchHit hit) {
            if (hit.getFields() != null && !hit.getFields().isEmpty() && result != null) {
                for (Field field : result.getClass().getDeclaredFields()) {
                    ScriptedField scriptedField = field.getAnnotation(ScriptedField.class);
                    if (scriptedField != null) {
                        String name = scriptedField.name().isEmpty() ? field.getName() : scriptedField.name();
                        DocumentField searchHitField = hit.getFields().get(name);
                        if (searchHitField != null) {
                            field.setAccessible(true);
                            try {
                                field.set(result, searchHitField.getValue());
                            } catch (IllegalArgumentException e) {
                                throw new ElasticsearchException(
                                        "failed to set scripted field: " + name + " with value: " + searchHitField.getValue(), e);
                            } catch (IllegalAccessException e) {
                                throw new ElasticsearchException("failed to access scripted field: " + name, e);
                            }
                        }
                    }
                }
            }
        }

        private <T> T mapEntity(Collection<DocumentField> values, Class<T> clazz) {
            return mapEntity(buildJSONFromFields(values), clazz);
        }

        private String buildJSONFromFields(Collection<DocumentField> values) {
            JsonFactory nodeFactory = new JsonFactory();
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                JsonGenerator generator = nodeFactory.createGenerator(stream, JsonEncoding.UTF8);
                generator.writeStartObject();
                for (DocumentField value : values) {
                    if (value.getValues().size() > 1) {
                        generator.writeArrayFieldStart(value.getName());
                        for (Object val : value.getValues()) {
                            generator.writeObject(val);
                        }
                        generator.writeEndArray();
                    } else {
                        generator.writeObjectField(value.getName(), value.getValue());
                    }
                }
                generator.writeEndObject();
                generator.flush();
                return new String(stream.toByteArray(), Charset.forName("UTF-8"));
            } catch (IOException e) {
                return null;
            }
        }
        private <T> void setPersistentEntityId(T result, String id, Class<T> clazz) {

            if (clazz.isAnnotationPresent(Document.class)) {

                ElasticsearchPersistentEntity<?> persistentEntity = mappingContext.getRequiredPersistentEntity(clazz);
                ElasticsearchPersistentProperty idProperty = persistentEntity.getIdProperty();

                // Only deal with String because ES generated Ids are strings !
                if (idProperty != null && idProperty.getType().isAssignableFrom(String.class)) {
                    persistentEntity.getPropertyAccessor(result).setProperty(idProperty, id);
                }
            }
        }

        private <T> void setPersistentEntityVersion(T result, long version, Class<T> clazz) {

            if (clazz.isAnnotationPresent(Document.class)) {

                ElasticsearchPersistentEntity<?> persistentEntity = mappingContext.getPersistentEntity(clazz);
                ElasticsearchPersistentProperty versionProperty = persistentEntity.getVersionProperty();

                // Only deal with Long because ES versions are longs !
                if (versionProperty != null && versionProperty.getType().isAssignableFrom(Long.class)) {
                    // check that a version was actually returned in the response, -1 would indicate that
                    // a search didn't request the version ids in the response, which would be an issue
                    Assert.isTrue(version != -1, "Version in response is -1");
                    persistentEntity.getPropertyAccessor(result).setProperty(versionProperty, version);
                }
            }
        }

        private <T> void setPersistentEntityScore(T result, float score, Class<T> clazz) {

            if (clazz.isAnnotationPresent(Document.class)) {

                ElasticsearchPersistentEntity<?> entity = mappingContext.getRequiredPersistentEntity(clazz);

                if (!entity.hasScoreProperty()) {
                    return;
                }
                entity.getPropertyAccessor(result) .setProperty(entity.getScoreProperty(), score);
            }
        }
    }
