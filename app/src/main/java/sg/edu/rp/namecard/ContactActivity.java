package sg.edu.rp.namecard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_SCAN = 1000;
    public static final int REQUEST_CODE_DELETE = 1001;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        initViews();
        initData();
    }

    private void initData() {
        refreshListView();
    }

    private void initViews() {
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent == null || parent.getAdapter() == null ||
                        parent.getAdapter().getItem(position) == null ||
                        !(parent.getAdapter().getItem(position) instanceof Contact)) {
                    return;
                }

                final Contact contact = (Contact) parent.getAdapter().getItem(position);

                final Bundle bundle = new Bundle();
                bundle.putSerializable("contact", contact);

                final Intent intent = new Intent(ContactActivity.this, DetailActivity.class);
                intent.putExtras(bundle);

                startActivityForResult(intent, REQUEST_CODE_DELETE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.AddContact) {
            final Intent intent = new Intent(this, AddContactActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(ContactActivity.this, CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SCAN);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SCAN) {
            if (null != data && data.getExtras() != null) {
                final Bundle bundle = data.getExtras();

                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    final String result = bundle.getString(CodeUtils.RESULT_STRING);
                    final Contact contact = convertToContact(result);

                    insertContact(contact);
                    refreshListView();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(ContactActivity.this, "Failed Resolving QR Code", Toast.LENGTH_LONG).show();
                }
            }
        }
        else if(requestCode == REQUEST_CODE_DELETE){
            refreshListView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshListView();
    }

    private void insertContact(Contact contact){
        if (contact != null) {
            final DBHelper dbHelper = new DBHelper(ContactActivity.this);
            dbHelper.insertContact(contact);
            dbHelper.close();
        }
    }

    private void refreshListView(){
        final DBHelper dbHelper = new DBHelper(ContactActivity.this);
        final ArrayList<Contact> allContacts = dbHelper.getAllContacts();
        dbHelper.close();
        listView.setAdapter(new ContactArrayAdapter(ContactActivity.this,R.layout.contact_row,allContacts));
    }

    private Contact convertToContact(String json) {
        try {
            final JSONObject jsonObject = new JSONObject(json);
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
}
