package sg.edu.rp.namecard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class AddContactActivity extends AppCompatActivity {



    private EditText etName, etMobile, etEmail, etCompany;
    private Button btnAddContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        etName = (EditText) findViewById(R.id.editTextName);
        etMobile = (EditText) findViewById(R.id.editTextMobile);
        etEmail = (EditText) findViewById(R.id.editTextEmail);
        etCompany = (EditText) findViewById(R.id.editTextCompany);
        btnAddContact = (Button) this.findViewById(R.id.button);



        btnAddContact.setOnClickListener(new View.OnClickListener() {
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

                        DBHelper dbHelper = new DBHelper(AddContactActivity.this);
                        dbHelper.insertContact(new Contact(name,mobile,email,company));
                        dbHelper.close();
                        Toast.makeText(AddContactActivity.this, "add contact succeed", Toast.LENGTH_SHORT).show();




                        finish();
                    } else {
                        Toast.makeText(AddContactActivity.this, "Text can not be empty", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean hasContent(){
        if(!TextUtils.isEmpty(etName.getText()) ||
                !TextUtils.isEmpty(etCompany.getText()) ||
                !TextUtils.isEmpty(etEmail.getText()) ||
                !TextUtils.isEmpty(etMobile.getText())){
            return true;
        }
        return false;
    }


}

