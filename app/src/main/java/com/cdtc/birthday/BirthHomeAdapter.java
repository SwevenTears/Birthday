package com.cdtc.birthday;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cdtc.birthday.util.BirthBean;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Sweven on 2018/9/23.
 * Email:sweventears@Foxmail.com
 */
public class BirthHomeAdapter extends RecyclerView.Adapter<BirthHomeAdapter.BirthHomeViewHolder> {

    private ArrayList<BirthBean> birthBeanArrayList;
    private LayoutInflater inflater;
    private Context context;

    private static final int backgroundImage[] = {
            R.drawable.home_background_01, R.drawable.home_background_02,
            R.drawable.home_background_03, R.drawable.home_background_04,
            R.drawable.home_background_05, R.drawable.home_background_06,
            R.drawable.home_background_07, R.drawable.home_background_08};

    private static final int foregroundImage[] = {
            R.drawable.home_foreground_01, R.drawable.home_foreground_02,
            R.drawable.home_foreground_03, R.drawable.home_foreground_04,
            R.drawable.home_foreground_05, R.drawable.home_foreground_06,
            R.drawable.home_foreground_07, R.drawable.home_foreground_08};

    private static final int fontColor[] = {
            Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
            Color.BLACK, Color.BLACK, Color.WHITE, Color.WHITE};

    BirthHomeAdapter(Context context, ArrayList<BirthBean> birthDate) {
        this.context = context;
        this.birthBeanArrayList = birthDate;
        birthBeanArrayList.add(0,new BirthBean());
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BirthHomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.panel_home_item, viewGroup, false);
        return new BirthHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BirthHomeViewHolder holder, int position) {
        String name = birthBeanArrayList.get(position).getName();
        String birthday = birthBeanArrayList.get(position).getBirthday();
        int imageStyle = birthBeanArrayList.get(position).getImageStyle();

        if (position == 0) {
            holder.setVisibility(false);
        }
        else {
            if (position % 5 == 0) {
                holder.setVisibility(true);
            }
            initTextView(holder, imageStyle);
            initData(holder, name, birthday, imageStyle);
            holder.HomePanelItemLayout.setBackgroundResource(backgroundImage[imageStyle]);
            holder.birthLayout.setBackgroundResource(foregroundImage[imageStyle]);
        }

    }

    @Override
    public int getItemCount() {
        return birthBeanArrayList.size();
    }

    /**
     * [初始化数据]
     *
     * @param holder     .
     * @param name       过生日人的名字
     * @param birthday   出生年月日
     * @param imageStyle 背景
     */
    @SuppressLint("SetTextI18n")
    private void initData(BirthHomeViewHolder holder, String name, String birthday, int imageStyle) {

        int birthYear = Integer.parseInt(birthday.split("-")[0]);
        int birthMonth = Integer.parseInt(birthday.split("-")[1]);
        int birthDate = Integer.parseInt(birthday.split("-")[2]);
        int age = DealHomeBirthDate.getAge(birthYear, birthMonth, birthDate);

        holder.birthCountDownText.setTextColor(fontColor[imageStyle]);
        holder.birthYearMonthText.setTextColor(fontColor[imageStyle]);

        Calendar now = Calendar.getInstance();

        if (name.equals("无记录")) {
            birthYear = now.get(Calendar.YEAR);
            birthMonth = now.get(Calendar.MONTH) + 1;
            birthDate = now.get(Calendar.DATE);
            holder.birthCountDownText.setText("您还未添加\n生日记录");
        } else {
            if (birthYear >= now.get(Calendar.YEAR)) {
                int remainTime = DealHomeBirthDate.getBetweenDays(birthYear, birthMonth, birthDate);
                holder.birthCountDownText.setText(name + "\n离出生还有\n" + remainTime + "天\no(ﾟДﾟ)っ！");
            } else {
                birthYear = now.get(Calendar.YEAR);
                if (birthMonth < now.get(Calendar.MONTH) + 1) {
                    birthYear = birthYear + 1;
                } else if (birthMonth == now.get(Calendar.MONTH) + 1) {
                    if (birthDate < now.get(Calendar.DATE)) {
                        birthYear = birthYear + 1;
                    }
                }
                int remainTime = DealHomeBirthDate.getBetweenDays(birthYear, birthMonth, birthDate);
                if (remainTime != 0) {
                    holder.birthCountDownText.setText(name + "\n" + age + "岁生日\n倒计时:\n" + remainTime + "天");
                } else {
                    holder.birthCountDownText.setText(name + "\n" + age + "岁生日\n就是今天!");
                }
            }
        }

        holder.birthYearMonthText.setText(birthYear + "年" + birthMonth + "月");
        holder.birthDateText.setText(birthDate + "");

        holder.birthWeekText.setText(DealHomeBirthDate.getWeekOfDate(birthYear, birthMonth, birthDate));
        // Extra Information
        holder.birthLunarYearText.setText("戊戌年[" + DealHomeBirthDate.animalsYear(birthYear) + "年]");
        holder.birthLunarMonthDateText.setText("八月" + "\t" + "十五");
        holder.birthConstellationText.setText(DealHomeBirthDate.constellation(birthMonth, birthDate));

    }

    /**
     * [代码添加新TextView]
     *
     * @param holder     View
     * @param imageStyle 文字颜色
     */
    private void initTextView(BirthHomeViewHolder holder, int imageStyle) {
        holder.birthWeekText = addTextView(imageStyle);
        holder.birthLunarYearText = addTextView(imageStyle);
        holder.birthLunarMonthDateText = addTextView(imageStyle);
        holder.birthConstellationText = addTextView(imageStyle);

        holder.birthWeekText.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);

        holder.birthLayout.removeAllViews();
        holder.birthLayout.addView(holder.birthYearMonthText);
        holder.birthLayout.addView(holder.birthDateText);
        holder.birthLayout.addView(holder.birthWeekText);
        holder.birthLayout.addView(holder.birthLunarYearText);
        holder.birthLayout.addView(holder.birthLunarMonthDateText);
        holder.birthLayout.addView(holder.birthConstellationText);

    }

    /**
     * [新建TextView]
     *
     * @param imageStyle 文字颜色
     * @return .
     */
    private TextView addTextView(int imageStyle) {
        TextView textView = new TextView(context);
        textView.setTextColor(fontColor[imageStyle]);
        textView.setTextSize(18);
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        textView.setLayoutParams(lp);

        return textView;
    }

    class BirthHomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView birthCountDownText, birthYearMonthText, birthDateText;
        private TextView birthWeekText, birthLunarYearText,
                birthLunarMonthDateText, birthConstellationText;

        private LinearLayout birthLayout;
        private LinearLayout HomePanelItemLayout;

        BirthHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            birthLayout = itemView.findViewById(R.id.birth_layout);
            birthCountDownText = itemView.findViewById(R.id.birth_count_down);
            birthYearMonthText = itemView.findViewById(R.id.birth_year_month);
            birthDateText = itemView.findViewById(R.id.birth_day);

            HomePanelItemLayout = itemView.findViewById(R.id.home_panel_item_layout);

            birthLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.birth_layout) {
                //TODO 打开信息详情页面
                Toast.makeText(context, "打开详情页", Toast.LENGTH_SHORT).show();
            }
        }

        public void setVisibility(boolean isVisible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (isVisible) {
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }
    }
}
