package issac.study.springdemo.model.vo;

/**
 * @author humy6
 */
public class UserVo {
    private Integer id;
    private String name;

    public UserVo() {
    }

    public UserVo(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
