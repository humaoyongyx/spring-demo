package issac.study.springdemo.model.req;

/**
 * @author humy6
 */
public class PageReq {
    private Integer page;
    private Integer size;

    public PageReq() {
    }

    public PageReq(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
