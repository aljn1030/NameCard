package sg.edu.rp.namecard;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    private TextView tvName, tvMobile, tvEmail, tvCompany;
    private Button btnDelete;

    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initViews();
        initData();
    }

    private void initData() {
        final Bundle extras = getIntent().getExtras();
        contact = (Contact) extras.getSerializable("contact");

        if(contact != null){
            tvName.setText(getResources().getString(R.string.name, contact.name));
            tvMobile.setText(getResources().getString(R.string.mobile, contact.mobile));
            tvEmail.setText(getResources().getString(R.string.email, contact.email));
            tvCompany.setText(getResources().getString(R.string.company, contact.company));
        }
    }

    private void initViews(){
        tvName = (TextView) findViewById(R.id.textViewName);
        tvMobile = (TextView) findViewById(R.id.textViewMobile);
        tvEmail = (TextView) findViewById(R.id.textViewEmail);
        tvCompany = (TextView) findViewById(R.id.textViewCompany);
        btnDelete = (Button) findViewById(R.id.btn_delete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new AlertDialog.Builder(DetailActivity.this).
                        setMessage("Delete or not?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final boolean deleteContact = deleteContact();
                        if(deleteContact){
                            setResult(RESULT_OK);
                            finish();
                        }else{
                            Toast.makeText(DetailActivity.this,"delete failed",Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

                dialog.show();
            }
        });
    }

    private boolean deleteContact(){
        if(contact !=null){
            final DBHelper dbHelper = new DBHelper(this);
            final int count = dbHelper.deleteById(contact.id);
            dbHelper.close();

            if(count != -1){
                return true;
            }
        }

        return false;
    }

}
