package card.example.com.card;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class Data_inputActivity extends AppCompatActivity {
    private EditText edit_1;
    private EditText group_name;
    private StringBuffer sb = new StringBuffer();
    private StringBuffer sb_group = new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_input);
        saveDate();




    }

    public void saveDate(){
        Button saveDate = (Button) findViewById(R.id.save_data);
        saveDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Boolean flag = true;
                edit_1 = (EditText) findViewById(R.id.edit_1);
                String input_edit_1 = edit_1.getText().toString().trim();

                group_name = (EditText) findViewById(R.id.group_name);
                String input_group_name =group_name.getText().toString().trim();
                if(group_name ==null || "".equals(group_name)){
                    Toast.makeText(Data_inputActivity.this, "请输入分组名！", Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
                    String[] temp = pref.getString("group_name","").split(",");
                    sb_group.append(pref.getString("group_name",""));


                    SharedPreferences.Editor group = getSharedPreferences("data",MODE_PRIVATE).edit();
                    for(int i=0;i<temp.length;i++){
                        if(input_group_name.equals(temp[i])){
                            flag=false;
                            break;
                        }
                    }
                    if(flag){

                        sb_group.append(input_group_name);
                        sb_group.append(",");
                        group.putString("group_name",sb_group.toString());
                        group.apply();
                    }
                }



                if(!(input_edit_1 == null  ||"".equals(input_edit_1))){
                    sb.append(input_edit_1);
                    sb.append(",");
                    SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putString("cards"+"_"+input_group_name,sb.toString());
                    editor.apply();
                    edit_1.setText("");



                    LinearLayout ll = (LinearLayout) findViewById(R.id.lay);
                        EditText tv = new EditText(Data_inputActivity.this);
//                        tv.setId(i);
                        tv.setText(String.valueOf(input_edit_1));
                        tv.setTextSize(20);
                        //3.把textView设置为线性布局的子节点
                        ll.addView(tv);
                }
            }
        });
    }
}
