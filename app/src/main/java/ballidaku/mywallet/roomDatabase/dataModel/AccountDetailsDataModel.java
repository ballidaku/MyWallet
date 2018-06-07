package ballidaku.mywallet.roomDatabase.dataModel;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.BaseObservable;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Observable;

import ballidaku.mywallet.commonClasses.MyConstant;

/**
 * Created by sharanpalsingh on 20/02/18.
 */

@Entity(tableName = MyConstant.ACCOUNT_DETAILS)
public class AccountDetailsDataModel extends BaseObservable implements Serializable
{

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = MyConstant.BANK_NAME)
    public String bankName;

    @ColumnInfo(name = MyConstant.ACCOUNT_HOLDER_NAME)
    public String accountHolderName;

    @ColumnInfo(name = MyConstant.ACCOUNT_NUMBER)
    public String accountNumber;

    @ColumnInfo(name = MyConstant.IFSC)
    public String ifsc;

    @ColumnInfo(name = MyConstant.ATM_NUMBER)
    public String atmNumber;

    @ColumnInfo(name = MyConstant.CVV)
    public String cvv;

    @ColumnInfo(name = MyConstant.VALID_FROM)
    public String validFrom;

    @ColumnInfo(name = MyConstant.VALID_THRU)
    public String validThru;

    @ColumnInfo(name = MyConstant.NET_BANKING_ID)
    public String netBankingId;

    @ColumnInfo(name = MyConstant.ADDITIONAL_DATA)
    public String additionalData;

    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getBankName()
    {
        return bankName;
    }

    public void setBankName(String bankName)
    {
        this.bankName = bankName;

        notifyChange();
    }

    public String getAccountHolderName()
    {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName)
    {
        this.accountHolderName = accountHolderName;

        notifyChange();
    }

    public String getAccountNumber()
    {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber)
    {
        this.accountNumber = accountNumber;
    }

    public String getIfsc()
    {
        return ifsc;
    }

    public void setIfsc(String ifsc)
    {
        this.ifsc = ifsc;
    }

    public String getAtmNumber()
    {
        return atmNumber;
    }

    public void setAtmNumber(String atmNumber)
    {
        this.atmNumber = atmNumber;
    }

    public String getCvv()
    {
        return cvv;
    }

    public void setCvv(String cvv)
    {
        this.cvv = cvv;
    }

    public String getValidFrom()
    {
        return validFrom;
    }

    public void setValidFrom(String validFrom)
    {
        this.validFrom = validFrom;
    }

    public String getValidThru()
    {
        return validThru;
    }

    public void setValidThru(String validThru)
    {
        this.validThru = validThru;
    }

    public String getNetBankingId()
    {
        return netBankingId;
    }

    public void setNetBankingId(String netBankingId)
    {
        this.netBankingId = netBankingId;
    }


}
