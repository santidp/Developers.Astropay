package apdprueba;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import com.sun.org.apache.bcel.internal.util.JavaWrapper;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import static com.sun.org.apache.xml.internal.serialize.OutputFormat.Defaults.Encoding;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SignatureException;
import java.util.Enumeration;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * Available functions: - newinvoice - get_status - get_exchange -
 * get_banks_by_country - confirmation
 */
/**
 * Class of AstroPay Streamline
 *
 * @author Santiago del Puerto (santiago@astropay.com)
 * @version 1.0
 *
 */
public class AstroPayStreamline {

    /**
     * ************************
     * Merchant configuration *
        *************************
     */
    private String x_login = "***";
    private String x_trans_key = "***";

    private String x_login_for_webpaystatus = "***";
    private String x_trans_key_for_webpaystatus = "***";

    private String secret_key = "***";

    private boolean sandbox = true;
    /**
     * *******************************
     * End of Merchant configuration *
         ********************************
     */

    /**
     * ***************************************************
     * ---- PLEASE DON'T CHANGE ANYTHING BELOW HERE ---- *
         ****************************************************
     */
    String[] url = {"", "", "", ""};
    /*
     url[0]:newinvoice
     url[1]:status
     url[2]:exchange
     url[3]:banks
     */

    private int errors = 0;

    final String formatter = "{0,10}{1,16}";

    static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    public void construct() {
        this.errors = 0;
        this.url[0] = "https://astropaycard.com/api_curl/streamline/newinvoice";
        this.url[1] = "https://astropaycard.com/apd/webpaystatus";
        this.url[2] = "https://astropaycard.com/apd/webcurrencyexchange";
        this.url[3] = "https://astropaycard.com/api_curl/apd/get_banks_by_country";

        if (this.sandbox) {
            this.url[0] = "http://sandbox.astropaycard.com/api_curl/streamline/newinvoice";
            this.url[1] = "http://sandbox.astropaycard.com/apd/webpaystatus";
            this.url[2] = "http://sandbox.astropaycard.com/apd/webcurrencyexchange";
            this.url[3] = "http://sandbox.astropaycard.com/api_curl/apd/get_banks_by_country";
        }
    }

    public String newinvoice(String invoice, float amount, String bank, String country, String iduser, String cpf, String name, String email, String currency, String description, String bdate, String address, String zip, String city, String state, String return_url, String confirmation_url) throws SignatureException, Exception// = "json")
    {
        Hashtable post_values = new Hashtable();

        //Mandatory
        post_values.put("x_login", this.x_login);
        post_values.put("x_trans_key", this.x_trans_key);
        post_values.put("x_invoice", invoice);
        post_values.put("x_amount", amount);
        post_values.put("x_bank", bank);
        post_values.put("type", "json");
        post_values.put("x_country", country);
        post_values.put("x_iduser", iduser);
        post_values.put("x_cpf", cpf);
        post_values.put("x_name", name);
        post_values.put("x_mail", email);
        //Optional
        post_values.put("x_currency", currency);
        post_values.put("x_description", description);
        post_values.put("x_bdate", bdate);
        post_values.put("x_address", address);
        post_values.put("x_zip", zip);
        post_values.put("x_city", city);
        post_values.put("x_state", state);
        post_values.put("x_return", return_url);
        post_values.put("x_confirm", confirmation_url);


        String message_to_control = invoice + "V" + amount +"I" + iduser + "2" + bank + "1" + cpf + "H" + bdate + "G" + email + "Y" + zip + "A" + address + "P" + city + "S" + state + "P";

        String control = getSign(this.secret_key, message_to_control);//hash_hmac("sha256", pack('A*', $message_to_control), pack('A*', this.secret_key));
        control = control.toUpperCase();

        post_values.put("control", control);

        String response = this.curl(this.url[0], post_values);
        return response;
    }

    public String get_status(String invoice) throws Exception {
        Hashtable post_values = new Hashtable();

        //Mandatory
        post_values.put("x_login", this.x_login_for_webpaystatus);
        post_values.put("x_trans_key", this.x_trans_key_for_webpaystatus);
        post_values.put("x_invoice", invoice);

        String response = this.curl(this.url[1], post_values);
        return response;
    }

    public String get_exchange(String country, float amount) throws Exception {
        Hashtable post_values = new Hashtable();

        //Mandatory
        post_values.put("x_login", this.x_login);
        post_values.put("x_country", country);
        post_values.put("x_amount", amount);

        String response = this.curl(this.url[2], post_values);
        return response;
    }

    public String get_banks_by_country(String country, String type) throws Exception {
        Hashtable post_values = new Hashtable();

        //Mandatory
        post_values.put("x_login", this.x_login);
        post_values.put("x_trans_key", this.x_trans_key);
        post_values.put("country_code", country);
        post_values.put("type", type);

        String response = this.curl(this.url[3], post_values);
        return response;
    }

    /**
     * END OF PUBLIC INTERFACE
     */
    private String curl(String url, Hashtable post_values) throws Exception {
        String post_string = "";
        String post_response = "";
        HttpURLConnection objRequest = null;
        
        Enumeration<String> enumKey = post_values.keys();
        while(enumKey.hasMoreElements()) {
            String key = enumKey.nextElement();
            String val = post_values.get(key)+"";
            post_string += key + "=" + val + "&";
            }
        post_string = post_string.substring(0, post_string.length()-1);
        try {
            // create an HttpURLConnection object to communicate with AstroPay transaction server
            objRequest = (HttpURLConnection) new URL(url).openConnection();
            objRequest.disconnect();
            objRequest.setRequestMethod("POST");
            objRequest.setDoOutput(true);
            // post data is sent as a stream
            objRequest.getOutputStream().write(post_string.getBytes("UTF-8"));
            objRequest.connect();
            /*
            objRequest.ContentLength = post_string.Length;
            objRequest.ContentType = "application/x-www-form-urlencoded, charset=utf-8";
            */

            
            // returned values are returned as a stream, then read into a string
            BufferedReader in = new BufferedReader(new InputStreamReader(objRequest.getInputStream(), "UTF-8"));
            InputStream ggh = objRequest.getInputStream();
            post_response = in.readLine();

            // the response string is broken into an array
            // The split character specified here must match the delimiting character specified above
        } catch (Exception e) {
            throw new Exception("Error ocurred in request");
        }
        objRequest.disconnect();
        return post_response;
    }

    public String confirmation(String result, String x_invoice, String x_iduser, String x_description, String x_document, String x_bank, String x_payment_type, String x_bank_name, float x_amount, String x_control) throws Exception {
        String post_response = this.get_status(x_invoice);
        //String[] response_array = post_response.split("|");
        //Must verify parameters
        return post_response;
    }
            
    public static String getSign(String key, String message) throws Exception{
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
        byte[] keyBytes = key.getBytes("UTF-8");
        byte[] messageBytes = message.getBytes("UTF-8");
        mac.init(new javax.crypto.spec.SecretKeySpec(keyBytes, mac.getAlgorithm()));
        return byteArray2HexString(mac.doFinal(messageBytes));
    }
    
    public static String byteArray2HexString(byte[] b){
        java.math.BigInteger bi = new java.math.BigInteger(1, b);
        return String.format("%0" + (b.length << 1) + "X", bi);
    }

}
