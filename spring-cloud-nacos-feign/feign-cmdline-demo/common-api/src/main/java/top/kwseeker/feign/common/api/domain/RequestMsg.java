package top.kwseeker.feign.common.api.domain;

public class RequestMsg<T> {

    T data;

    public RequestMsg(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
