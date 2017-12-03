package card.example.com.card;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView show_card;
    private List<String> cardList;
    private List<String> group_name;
    private TimeCount timeCount;
    private  int allCard = 0;
    private  int right = 0;
    private  int wrong = 0;
    private  int pass = 0;
    private  int status = 0;    //游戏状态 0 结束，1，开始
    private TextView result;
    List<String> mCountries ;

    private ArrayAdapter<String> adapter;
    private Spinner  sp;


    @Override
    protected void onResume() {
        super.onResume();
        //数据增加后自动更新maninAcitvity的数据
//        cardList= get_show_card();
        group_name_init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView timer = (TextView) findViewById(R.id.timer);
        timer.setText("总时间：3分0秒");




//        cardList= get_show_card();
//        allCard = cardList.size();
//        result = (TextView) findViewById(R.id.result);
//        result.setText("共：" + allCard + "题  " );



        group_name_init();


        //错
         final Button wrong = (Button) findViewById(R.id.worng);
        wrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status == 1){
                    MainActivity.this.wrong++;
                    show_result();
                    random_show_card(cardList);
                }

            }
        });


        //对
        Button right = (Button) findViewById(R.id.right);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status == 1){

                    MainActivity.this.right++;
                    show_result();
                    random_show_card(cardList);
                }
            }
        });




        //开始
        Button start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //标记游戏开始
                MainActivity.this.status = 1;
                //从资源文件中取得卡的列表
                cardList= get_show_card( (String) sp.getSelectedItem());
                //初始化数据
                MainActivity.this.wrong = 0;
                MainActivity.this.right = 0;
                MainActivity.this.pass = 0;

                //开始计分
                allCard = cardList.size();
                result = (TextView) findViewById(R.id.result);
                result.setText("共" + allCard + "题  " + "对0" + " 错0" + " 过0");

                //随机显示一张
                random_show_card(cardList);


                //打开计时器
                if(timeCount != null){
                    timeCount.cancel();
                }
                timeCount = new TimeCount(180000, 1000);
                timeCount.start();

            }
        });

        //结束
        Button stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeCount.cancel();
                MainActivity.this.status=0;

            }
        });



        //点击下一条单击事件
        Button restoreDate = (Button) findViewById(R.id.restore_data);
        restoreDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
//                String cards = pref.getString("cards","");
//                Log.d("MainActivity","cards:" + cards);
                if(status == 1){

                    random_show_card(cardList);
                    MainActivity.this.pass++;
                    show_result();
                }

            }
        });
    }


    public void init_show_card(){
        show_card = (TextView) findViewById(R.id.show_card);
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        String[] cards = pref.getString("cards","").split(",");
        show_card.setGravity(Gravity.CENTER);
        show_card.setText(Arrays.toString(cards));
    }

    public void random_show_card(List<String> list){
        if(list.size()>0){
            int random = new Random().nextInt(list.size());
            show_card = (TextView) findViewById(R.id.show_card);
            show_card.setGravity(Gravity.CENTER);
            show_card.setText(list.get(random));
            list.remove(random);
            cardList = list;
        }else{
            show_card = (TextView) findViewById(R.id.show_card);
            show_card.setGravity(Gravity.CENTER);
            show_card.setText("已结束");
            timeCount.cancel();
            status = 0;

        }

    }

    public List<String> get_show_card(String group_name){
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        String[] cards = pref.getString("cards"+"_"+group_name,"").split(",");
        List<String> list = new ArrayList<String>(Arrays.asList(cards));
        return list;
    }

    public List<String> get_group_name(){
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        String[] group_name = pref.getString("group_name","").split(",");
        List<String> list = new ArrayList<String>(Arrays.asList(group_name));
        return list;
    }

    public void group_name_init(){
        //取值
        group_name =get_group_name();
        mCountries = group_name;
        sp = (Spinner) findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mCountries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);//设置到界面呢

        sp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中
//                Toast.makeText(MainActivity.this, adapter.getItem(arg2), Toast.LENGTH_SHORT).show();

                cardList= get_show_card(adapter.getItem(arg2));
                allCard = cardList.size();
                result = (TextView) findViewById(R.id.result);
                result.setText("共：" + allCard + "题  " );

//                Toast.makeText(MainActivity.this, (String) sp.getSelectedItem(), Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                Toast.makeText(MainActivity.this, "nothing", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
//                Toast.makeText(this, "you click Adddffdfdd_", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,Data_inputActivity.class);
                startActivity(intent);
                break;
            case R.id.remove_item:
                Toast.makeText(this, "you click remove", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }






    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {//计时完毕时触发
            // 倒计时结束后的方法
            TextView timer = (TextView) findViewById(R.id.timer);
            timer.setText("游戏已结束");
            MainActivity.this.status =0;

        }
        @Override
        public void onTick(long millisUntilFinished){//计时过程显示
            TextView timer = (TextView) findViewById(R.id.timer);
            timer.setText("剩余："+millisUntilFinished / 1000/60 +"分" + millisUntilFinished / 1000%60+"秒");



        }
    }

    Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (timeCount!=null) {
                timeCount.cancel();
            }
        }
    };

    public void show_result(){
        result = (TextView) findViewById(R.id.result);
        result.setText("共" + allCard + "题 " + " 对" + right + " 错" + wrong + " 过" + pass);
    }

}
