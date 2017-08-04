package sg.edu.rp.namecard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.uuzuche.lib_zxing.encoding.EncodingHandler;

import org.json.JSONException;
import org.json.JSONObject;


public class MyActivity extends AppCompatActivity {

    private EditText etName, etMobile, etEmail, etCompany;
    private ImageView imageView;
    private Button generateQRCodeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        etName = (EditText) findViewById(R.id.editTextName);
        etMobile = (EditText) findViewById(R.id.editTextMobile);
        etEmail = (EditText) findViewById(R.id.editTextEmail);
        etCompany = (EditText) findViewById(R.id.editTextCompany);
        imageView = (ImageView) findViewById(R.id.imageViewQR);
        generateQRCodeButton = (Button) this.findViewById(R.id.button);

        showMyInfo();

        generateQRCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String name = etName.getText() != null ? etName.getText().toString().trim() : "";
                    final String mobile = etMobile.getText() != null ? etMobile.getText().toString().trim() : "";
                    final String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
                    final String company = etCompany.getText() != null ? etCompany.getText().toString().trim() : "";

                    final JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", name);
                    jsonObject.put("mobile", mobile);
                    jsonObject.put("email", email);
                    jsonObject.put("company", company);

                    if (hasContent()) {
                        Bitmap qrCodeBitmap = EncodingHandler.createQRCode(jsonObject.toString(), dpToPx(MyActivity.this, 100F));
                        imageView.setImageBitmap(qrCodeBitmap);
                    } else {
                        Toast.makeText(MyActivity.this, "Text can not be empty", Toast.LENGTH_SHORT).show();
                    }

                    saveInfo(jsonObject.toString());


                } catch (WriterException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showMyInfo() {
        Contact info = getMyContact();
        if(info != null){
            etName.setText(info.name);
            etMobile.setText(info.mobile);
            etEmail.setText(info.email);
            etCompany.setText(info.company);

            try {
                Bitmap qrCodeBitmap = EncodingHandler.createQRCode(getMyContactJson(), dpToPx(MyActivity.this, 100F));
                imageView.setImageBitmap(qrCodeBitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean hasContent() {
        if (!TextUtils.isEmpty(etName.getText()) ||
                !TextUtils.isEmpty(etCompany.getText()) ||
                !TextUtils.isEmpty(etEmail.getText()) ||
                !TextUtils.isEmpty(etMobile.getText())) {
            return true;
        }
        return false;
    }

    public static int dpToPx(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }



    private void saveInfo(String result) {
        SharedPreferences sp = getSharedPreferences("sp_file", Context.MODE_PRIVATE);
        sp.edit().remove("contact").apply();
        sp.edit().putString("contact", result).apply();
    }

    private Contact getMyContact() {
        SharedPreferences sp = getSharedPreferences("sp_file", Context.MODE_PRIVATE);
        String contact = sp.getString("contact", null);

        if(TextUtils.isEmpty(contact)){
            return null;
        }


        try {
            JSONObject jsonObject = new JSONObject(contact);
            return new Contact(
                    jsonObject.getString("name"),
                    jsonObject.getString("mobile"),
                    jsonObject.getString("email"),
                    jsonObject.getString("company"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getMyContactJson() {
        SharedPreferences sp = getSharedPreferences("sp_file", Context.MODE_PRIVATE);
        String contact = sp.getString("contact", null);

        if(TextUtils.isEmpty(contact)){
            return null;
        }

        try {
            return new JSONObject(contact).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}

