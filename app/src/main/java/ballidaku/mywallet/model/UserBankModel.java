package ballidaku.mywallet.model;

import java.io.Serializable;

/**
 * Created by sharanpalsingh on 30/03/17.
 */
public class UserBankModel implements Serializable
{

    String valid_thru;
    String account_holder_name;
    String valid_from;
    String cvv;
    String ifsc;
    String bank_name;
    String net_banking_id;
    String atm_number;
    String additional_data;

    public UserBankModel()
    {
    }

    String account_number;

    public UserBankModel(String valid_thru, String account_holder_name, String valid_from, String cvv, String ifsc, String bank_name, String net_banking_id, String atm_number, String account_number, String additional_data )
    {
        this.valid_thru = valid_thru;
        this.account_holder_name = account_holder_name;
        this.valid_from = valid_from;
        this.cvv = cvv;
        this.ifsc = ifsc;
        this.bank_name = bank_name;
        this.net_banking_id = net_banking_id;
        this.atm_number = atm_number;
        this.account_number = account_number;
        this.additional_data = additional_data;
    }

    public String getValid_thru()
    {
        return valid_thru;
    }

    public String getAccount_holder_name()
    {
        return account_holder_name;
    }

    public String getValid_from()
    {
        return valid_from;
    }

    public String getCvv()
    {
        return cvv;
    }

    public String getIfsc()
    {
        return ifsc;
    }

    public String getBank_name()
    {
        return bank_name;
    }

    public String getNet_banking_id()
    {
        return net_banking_id;
    }

    public String getAtm_number()
    {
        return atm_number;
    }

    public String getAccount_number()
    {
        return account_number;
    }

    public String getAdditional_data()
    {
        return additional_data;
    }
}
