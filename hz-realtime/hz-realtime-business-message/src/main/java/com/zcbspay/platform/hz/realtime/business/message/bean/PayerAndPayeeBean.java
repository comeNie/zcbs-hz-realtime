package com.zcbspay.platform.hz.realtime.business.message.bean;

public class PayerAndPayeeBean implements java.io.Serializable {

    private static final long serialVersionUID = 3368208455848802132L;
    private String debtorname;
    private String debtoraccountno;
    private String debtorbranchcode;
    private String creditorbranchcode;
    private String creditorname;
    private String creditoraccountno;
    private Long amount;

    public String getDebtorname() {
        return debtorname;
    }

    public void setDebtorname(String debtorname) {
        this.debtorname = debtorname;
    }

    public String getDebtoraccountno() {
        return debtoraccountno;
    }

    public void setDebtoraccountno(String debtoraccountno) {
        this.debtoraccountno = debtoraccountno;
    }

    public String getDebtorbranchcode() {
        return debtorbranchcode;
    }

    public void setDebtorbranchcode(String debtorbranchcode) {
        this.debtorbranchcode = debtorbranchcode;
    }

    public String getCreditorbranchcode() {
        return creditorbranchcode;
    }

    public void setCreditorbranchcode(String creditorbranchcode) {
        this.creditorbranchcode = creditorbranchcode;
    }

    public String getCreditorname() {
        return creditorname;
    }

    public void setCreditorname(String creditorname) {
        this.creditorname = creditorname;
    }

    public String getCreditoraccountno() {
        return creditoraccountno;
    }

    public void setCreditoraccountno(String creditoraccountno) {
        this.creditoraccountno = creditoraccountno;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PayerAndPayeeBean [debtorname=" + debtorname + ", debtoraccountno=" + debtoraccountno + ", debtorbranchcode=" + debtorbranchcode + ", creditorbranchcode=" + creditorbranchcode
                + ", creditorname=" + creditorname + ", creditoraccountno=" + creditoraccountno + ", amount=" + amount + "]";
    }

}
