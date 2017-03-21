package org.hz.realtime.business.message.bean;

import java.io.Serializable;

public class ResponseInfo implements Serializable {

    private static final long serialVersionUID = -8802504869824102589L;
    /**
     * 业务状态
     */
    private String Sts;
    /**
     * 业务拒绝处理码
     */
    private String RjctCd;
    /**
     * 业务拒绝信息
     */
    private String RjctInf;
    /**
     * 轧差日期
     */
    private String NetgDt;

    public String getSts() {
        return Sts;
    }

    public void setSts(String sts) {
        Sts = sts;
    }

    public String getRjctCd() {
        return RjctCd;
    }

    public void setRjctCd(String rjctCd) {
        RjctCd = rjctCd;
    }

    public String getRjctInf() {
        return RjctInf;
    }

    public void setRjctInf(String rjctInf) {
        RjctInf = rjctInf;
    }

    public String getNetgDt() {
        return NetgDt;
    }

    public void setNetgDt(String netgDt) {
        NetgDt = netgDt;
    }

    @Override
    public String toString() {
        return "ResponseInfo [Sts=" + Sts + ", RjctCd=" + RjctCd + ", RjctInf=" + RjctInf + ", NetgDt=" + NetgDt + "]";
    }

}
