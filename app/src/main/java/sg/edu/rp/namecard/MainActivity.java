package sg.edu.rp.namecard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView iv1, iv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv1 = (ImageView) findViewById(R.id.imageView);
        iv2 = (ImageView) findViewById(R.id.imageView2);

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, MyActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, ContactActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
    }

}
