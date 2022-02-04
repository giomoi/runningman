package vn.co.vns.runningman.error;

/**
 * Created by thanhnv on 7/13/17.
 */

public class ErrorView {

    private String msg;
    private int status;

    public ErrorView(String msg,int status) {
        this.msg = msg;
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public int getStatus(){
        return status;
    }

    @Override
    public String toString() {
        return "Error{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

}
