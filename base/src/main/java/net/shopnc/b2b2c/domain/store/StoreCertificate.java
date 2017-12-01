package net.shopnc.b2b2c.domain.store;

import javax.persistence.*;

/**
 * 商家资质实体
 * Created by dqw on 2015/12/09.
 */
@Entity
@Table(name = "store_certificate")
public class StoreCertificate {
    /**
     * 商家编号
     */
    @Id
    @Column(name = "seller_id")
    private int sellerId;
    /**
     * 商家用户名
     */
    @Column(name = "seller_name")
    private String sellerName;
    /**
     * 公司名称
     */
    @Column(name = "company_name")
    private String companyName;
    /**
     * 公司地址编号
     */
    @Column(name = "company_address_id")
    private int companyAddressId;
    /**
     * 公司地址
     */
    @Column(name = "company_address")
    private String companyAddress;
    /**
     * 公司详细地址
     */
    @Column(name = "company_address_detail")
    private String companyAddressDetail;
    /**
     * 公司电话
     */
    @Column(name = "company_phone")
    private String companyPhone;
    /**
     * 公司员工总数
     */
    @Column(name = "company_employee_count")
    private int companyEmployeeCount;
    /**
     * 注册资金
     */
    @Column(name = "company_registered_capital")
    private int companyRegisteredCapital;
    /**
     * 联系人姓名
     */
    @Column(name = "contacts_name")
    private String contactsName;
    /**
     * 联系人电话
     */
    @Column(name = "contacts_phone")
    private String contactsPhone;
    /**
     * 联系人邮箱
     */
    @Column(name = "contacts_email")
    private String contactsEmail;
    /**
     * 营业执照号
     */
    @Column(name = "business_licence_number")
    private String businessLicenceNumber;
    /**
     * 法定经营范围
     */
    @Column(name = "business_sphere")
    private String businessSphere;
    /**
     * 营业执照电子版
     */
    @Column(name = "business_licence_image")
    private String businessLicenceImage;
    /**
     * 组织机构代码
     */
    @Column(name = "organization_code")
    private String organizationCode;
    /**
     * 组织机构代码电子版
     */
    @Column(name = "organization_image")
    private String organizationImage;
    /**
     * 一般纳税人证明
     */
    @Column(name = "general_taxpayer")
    private String generalTaxpayer;
    /**
     * 银行开户名
     */
    @Column(name = "bank_account_name")
    private String bankAccountName;
    /**
     * 银行账号
     */
    @Column(name = "bank_account_number")
    private String bankAccountNumber;
    /**
     * 开户银行名称
     */
    @Column(name = "bank_name")
    private String bankName;
    /**
     * 支行联行号
     */
    @Column(name = "bank_code")
    private String bankCode;
    /**
     * 开户银行所在地
     */
    @Column(name = "bank_address")
    private String bankAddress;
    /**
     * 开户银行许可证电子版
     */
    @Column(name = "bank_licence_image")
    private String bankLicenceImage;
    /**
     * 结算银行开户名
     */
    @Column(name = "settlement_bank_account_name")
    private String settlementBankAccountName;
    /**
     * 结算公司银行账号
     */
    @Column(name = "settlement_bank_account_number")
    private String settlementBankAccountNumber;
    /**
     * 结算开户银行名称
     */
    @Column(name = "settlement_bank_name")
    private String settlementBankName;
    /**
     * 结算支行联行号
     */
    @Column(name = "settlement_bank_code")
    private String settlementBankCode;
    /**
     * 结算开户银行所在地
     */
    @Column(name = "settlement_bank_address")
    private String settlementBankAddress;
    /**
     * 税务登记证号
     */
    @Column(name = "tax_registration_certificate")
    private String taxRegistrationCertificate;
    /**
     * 纳税人识别号
     */
    @Column(name = "taxpayer_id")
    private String taxpayerId;
    /**
     * 税务登记证号电子版
     */
    @Column(name = "tax_registration_image")
    private String taxRegistrationImage;

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getCompanyAddressId() {
        return companyAddressId;
    }

    public void setCompanyAddressId(int companyAddressId) {
        this.companyAddressId = companyAddressId;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyAddressDetail() {
        return companyAddressDetail;
    }

    public void setCompanyAddressDetail(String companyAddressDetail) {
        this.companyAddressDetail = companyAddressDetail;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public int getCompanyEmployeeCount() {
        return companyEmployeeCount;
    }

    public void setCompanyEmployeeCount(int companyEmployeeCount) {
        this.companyEmployeeCount = companyEmployeeCount;
    }

    public int getCompanyRegisteredCapital() {
        return companyRegisteredCapital;
    }

    public void setCompanyRegisteredCapital(int companyRegisteredCapital) {
        this.companyRegisteredCapital = companyRegisteredCapital;
    }

    public String getContactsName() {
        return contactsName;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }

    public String getContactsEmail() {
        return contactsEmail;
    }

    public void setContactsEmail(String contactsEmail) {
        this.contactsEmail = contactsEmail;
    }

    public String getBusinessLicenceNumber() {
        return businessLicenceNumber;
    }

    public void setBusinessLicenceNumber(String businessLicenceNumber) {
        this.businessLicenceNumber = businessLicenceNumber;
    }

    public String getBusinessSphere() {
        return businessSphere;
    }

    public void setBusinessSphere(String businessSphere) {
        this.businessSphere = businessSphere;
    }

    public String getBusinessLicenceImage() {
        return businessLicenceImage;
    }

    public void setBusinessLicenceImage(String businessLicenceImage) {
        this.businessLicenceImage = businessLicenceImage;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationImage() {
        return organizationImage;
    }

    public void setOrganizationImage(String organizationImage) {
        this.organizationImage = organizationImage;
    }

    public String getGeneralTaxpayer() {
        return generalTaxpayer;
    }

    public void setGeneralTaxpayer(String generalTaxpayer) {
        this.generalTaxpayer = generalTaxpayer;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBankLicenceImage() {
        return bankLicenceImage;
    }

    public void setBankLicenceImage(String bankLicenceImage) {
        this.bankLicenceImage = bankLicenceImage;
    }

    public String getSettlementBankAccountName() {
        return settlementBankAccountName;
    }

    public void setSettlementBankAccountName(String settlementBankAccountName) {
        this.settlementBankAccountName = settlementBankAccountName;
    }

    public String getSettlementBankAccountNumber() {
        return settlementBankAccountNumber;
    }

    public void setSettlementBankAccountNumber(String settlementBankAccountNumber) {
        this.settlementBankAccountNumber = settlementBankAccountNumber;
    }

    public String getSettlementBankName() {
        return settlementBankName;
    }

    public void setSettlementBankName(String settlementBankName) {
        this.settlementBankName = settlementBankName;
    }

    public String getSettlementBankCode() {
        return settlementBankCode;
    }

    public void setSettlementBankCode(String settlementBankCode) {
        this.settlementBankCode = settlementBankCode;
    }

    public String getSettlementBankAddress() {
        return settlementBankAddress;
    }

    public void setSettlementBankAddress(String settlementBankAddress) {
        this.settlementBankAddress = settlementBankAddress;
    }

    public String getTaxRegistrationCertificate() {
        return taxRegistrationCertificate;
    }

    public void setTaxRegistrationCertificate(String taxRegistrationCertificate) {
        this.taxRegistrationCertificate = taxRegistrationCertificate;
    }

    public String getTaxpayerId() {
        return taxpayerId;
    }

    public void setTaxpayerId(String taxpayerId) {
        this.taxpayerId = taxpayerId;
    }

    public String getTaxRegistrationImage() {
        return taxRegistrationImage;
    }

    public void setTaxRegistrationImage(String taxRegistrationImage) {
        this.taxRegistrationImage = taxRegistrationImage;
    }

    @Override
    public String toString() {
        return "StoreCertificate{" +
                "sellerId=" + sellerId +
                ", sellerName='" + sellerName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyAddressId=" + companyAddressId +
                ", companyAddress='" + companyAddress + '\'' +
                ", companyAddressDetail='" + companyAddressDetail + '\'' +
                ", companyPhone='" + companyPhone + '\'' +
                ", companyEmployeeCount=" + companyEmployeeCount +
                ", companyRegisteredCapital=" + companyRegisteredCapital +
                ", contactsName='" + contactsName + '\'' +
                ", contactsPhone='" + contactsPhone + '\'' +
                ", contactsEmail='" + contactsEmail + '\'' +
                ", businessLicenceNumber='" + businessLicenceNumber + '\'' +
                ", businessSphere='" + businessSphere + '\'' +
                ", businessLicenceImage='" + businessLicenceImage + '\'' +
                ", organizationCode='" + organizationCode + '\'' +
                ", organizationImage='" + organizationImage + '\'' +
                ", generalTaxpayer='" + generalTaxpayer + '\'' +
                ", bankAccountName='" + bankAccountName + '\'' +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", bankAddress='" + bankAddress + '\'' +
                ", bankLicenceImage='" + bankLicenceImage + '\'' +
                ", settlementBankAccountName='" + settlementBankAccountName + '\'' +
                ", settlementBankAccountNumber='" + settlementBankAccountNumber + '\'' +
                ", settlementBankName='" + settlementBankName + '\'' +
                ", settlementBankCode='" + settlementBankCode + '\'' +
                ", settlementBankAddress='" + settlementBankAddress + '\'' +
                ", taxRegistrationCertificate='" + taxRegistrationCertificate + '\'' +
                ", taxpayerId='" + taxpayerId + '\'' +
                ", taxRegistrationImage='" + taxRegistrationImage + '\'' +
                '}';
    }
}

