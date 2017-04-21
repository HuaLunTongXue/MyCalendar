package kevin.android.com.mycalendar;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    private TextView showCalendar;
    private TableLayout tableLayoutCalendar;
    private TableRow tableRowCalendar;
    private TextView tvTitle;
    private ImageView imageLeft;
    private ImageView imageRight;
    private Date mDate;
    private SimpleDateFormat mSimpleDateFormat;
    private RelativeLayout relativeLayoutTitle;
    private Date mCurrentDate;
    private TextView tvToday;
    private Date mShowDate;
    private Toast mToast;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showCalendar = (TextView) findViewById(R.id.showCalendar);
        tableLayoutCalendar = (TableLayout) findViewById(R.id.tableLayoutCalendar);
        tableRowCalendar = (TableRow) findViewById(R.id.tableRowCalendar);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        imageLeft = (ImageView) findViewById(R.id.imageLeft);
        imageRight = (ImageView) findViewById(R.id.imageRight);
        relativeLayoutTitle = (RelativeLayout) findViewById(R.id.relativeLayoutTitle);
        tvToday = (TextView) findViewById(R.id.tvToday);

//        SimpleDateFormat sf = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        mSimpleDateFormat = new SimpleDateFormat("yyyy年MM月");
        mDate = new Date();
        mCurrentDate = new Date();
        tvTitle.setText(mSimpleDateFormat.format(mDate));
        getDate(mDate);

        imageLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableRowCalendar.removeAllViews();
                tableLayoutCalendar.removeAllViews();
                tableLayoutCalendar.addView(relativeLayoutTitle);
                tableLayoutCalendar.addView(tableRowCalendar);
                mDate.setMonth(mDate.getMonth() - 1);
                Log.e(TAG, "mDate=" + mDate);
                tvTitle.setText(mSimpleDateFormat.format(mDate));
                getDate(mDate);
            }
        });

        imageRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableRowCalendar.removeAllViews();
                tableLayoutCalendar.removeAllViews();
                tableLayoutCalendar.addView(relativeLayoutTitle);
                tableLayoutCalendar.addView(tableRowCalendar);
                mDate.setMonth(mDate.getMonth() + 1);
                Log.e(TAG, "mDate=" + mDate);
                tvTitle.setText(mSimpleDateFormat.format(mDate));
                getDate(mDate);
            }
        });

        tvToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShowDate != null) {
                    if (mShowDate.getYear() == mCurrentDate.getYear()
                        && mShowDate.getMonth() == mCurrentDate.getMonth()
                        && mShowDate.getDate() == mCurrentDate.getDate()) {
                        showInfo("已经回到今天！！！");
                        return;
                    }
                }
                Log.e(TAG,"Return today mDate        ="+mDate);
                Log.e(TAG,"Return today mCurrentDate ="+mCurrentDate);
                mDate = new Date();
                tableRowCalendar.removeAllViews();
                tableLayoutCalendar.removeAllViews();
                tableLayoutCalendar.addView(relativeLayoutTitle);
                tableLayoutCalendar.addView(tableRowCalendar);
                tvTitle.setText(mSimpleDateFormat.format(mCurrentDate));
                getDate(mCurrentDate);
            }
        });

    }


    public void getDate(Date nowDate) {
        mShowDate = nowDate;
        Log.e(TAG, "nowData     =" + nowDate.getMonth());
        Log.e(TAG, "mCurrentDate=" + mCurrentDate.getMonth());
        Calendar cad = Calendar.getInstance();
        cad.setTime(nowDate);
        int day_month = cad.getActualMaximum(Calendar.DAY_OF_MONTH); //获取当月天数
        int[][] array = new int[6][7];
        for (int i = 0; i <= day_month - 1; i++) {               //循环遍历每天

            cad.set(Calendar.DAY_OF_MONTH, i + 1);

            int week_month = cad.get(Calendar.WEEK_OF_MONTH);  //获取改天在本月的第几个星期，也就是第几行

            int now_day_month = cad.get(Calendar.DAY_OF_WEEK);   //获取该天在本星期的第几天  ，也就是第几列

            array[week_month - 1][now_day_month - 1] = i + 1;              //将该天存放到二位数组中

        }

        String[] weeks = {"日", "一", "二", "三", "四", "五", "六"};
        for (int i = 0; i < weeks.length; i++) {
            if (i == 0 || i == weeks.length - 1) {
                tableRowCalendar.addView(getTextView(weeks[i], true, Color.RED, false));
            } else {
                tableRowCalendar.addView(getTextView(weeks[i], false, 0, false));
            }
        }

//        for (String w : weeks) {
//            showCalendar.append(w + "\t");
//            tableRowCalendar.addView(getTextView(w, false, 0, false));
//        }

        showCalendar.append("\n");

        for (int i = 0; i <= array.length - 1; i++) {

            TableRow tableRow = new TableRow(this);
            ViewGroup.LayoutParams
                    layoutParams =
                    new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                               ViewGroup.LayoutParams.WRAP_CONTENT);
            tableRow.setLayoutParams(layoutParams);

            for (int j = 0; j <= array[i].length - 1; j++) {

                if (array[i][j] != 0) {                     //没有存放的数字默认为0，过滤
                    showCalendar.append(String.valueOf(array[i][j]));
                }

                if (array[i][j] != 0) {

                    if (array[i][j] == mCurrentDate.getDate() &&
                        nowDate.getMonth() == mCurrentDate.getMonth() &&
                        nowDate.getYear() == mCurrentDate.getYear()) {

                        if((j) % 7 == 0||(j+1) % 7 == 0){ //周六和周日红色字体
                            tableRow.addView(getTextView(String.valueOf(array[i][j]), true, Color.RED, true));
                        }else{
                            tableRow.addView(getTextView(String.valueOf(array[i][j]), true, Color.WHITE, true));
                        }

                    } else {
                        if((j) % 7 == 0||(j+1) % 7 == 0){
                            tableRow.addView(getTextView(String.valueOf(array[i][j]), true, Color.RED, false));
                        }else{
                            tableRow.addView(getTextView(String.valueOf(array[i][j]), false, 0, false));
                        }
                    }
                } else {
                    tableRow.addView(getTextView("", false, 0, false));
                }

                showCalendar.append("\t");
                if ((j + 1) % 7 == 0) {
                    showCalendar.append("\n");
                }
            }
            tableLayoutCalendar.addView(tableRow);
        }

    }


    private TextView getTextView(String text, boolean isSetTextColor, int color, boolean isSetBg) {
        TextView textView = new TextView(this);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                                       ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 5, 5, 5);
        layoutParams.topMargin = 30;
        layoutParams.bottomMargin = 30;
        layoutParams.weight = 1;
        textView.setText(text);
        if (isSetTextColor) {
            textView.setTextColor(color);
        } else {
            textView.setTextColor(Color.BLACK);
        }
        if (isSetBg) {
            textView.setBackgroundResource(R.drawable.tv_bg);
        }
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    private void showInfo(String data) {
        if (mToast == null) {
            mToast = Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(data);
        }
        mToast.show();
    }

}
