package com.example.test1;


import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
class Parse {
    //全局错误判断
    public static boolean error=false;
    public int index=0;
    public int len=0;
    public double get_data(String str){
        double re=0;
        double step=10;
        while (true){
            if(len<index){
                break;
            }
            char temp=str.charAt(index);
            if(temp>='0'&&temp<='9'){
                re=re*step+(temp-'0');
                ++index;
            }else if(temp=='.'){
                ++index;
                re+=get_point(str);
                break;
            }else {
                break;
            }
        }
        return re;
    }
    public double get_point(String str){
        double re=0;
        double step=10;
        while (true){
            if(len<index){
                break;
            }
            char temp=str.charAt(index);
            if(temp>='0'&&temp<='9'){
                re+=(str.charAt(index++)-'0')/step;
                step*=10;
            }else{
                break;
            }
        }
        return re;
    }
    public double __get_data(String str){
        double re=0;
        if(str.charAt(index)=='-'){
            ++index;
            re-=get_data(str);
        }else if(str.charAt(index)=='√'){
            index+=2;
            double get_re=get_data(str);
            if(get_re<0){
                //错误处理
                error=true;
                return 0.0;
            }else {
                re=Math.sqrt(get_re);
            }
        }else {
            re+=get_data(str);
        }
        return  re;
    }
    public int get_end(String str){
        int end=str.length()-1;
        int re=0;
        for(re=end;re>=0;--re){
            if(str.charAt(re)>='0'&&str.charAt(re)<='9'){
                return re;
            }
        }
        return re;
    }
    public double parse(String str){
        if(str.isEmpty()||(str.length()==1&&str.charAt(0)=='-')||(str.length()==2&&str.charAt(0)=='√')){
            return 0.0;
        }
        if(error){
            return  0.0;
        }
        len=get_end(str);
        double re=0;
        if(str.charAt(index)=='-'){
            ++index;
            re-=__get_data(str);
        }else {
            re=(__get_data(str));
        }
        while (true){
            if(len<index){
                break;
            }
            char temp=str.charAt(index);
            if(temp=='+'){
                ++index;
                re+=parse(str);
            }else if(temp=='-'){
                re+=parse(str);
            }
            else if(str.charAt(index)=='*'){
                ++index;
                if(str.charAt(index)=='-'){
                    ++index;
                    re*=(0-__get_data(str));
                }else {
                    re*=(__get_data(str));
                }
            }else if(str.charAt(index)=='/'){
                ++index;
                if(str.charAt(index)=='-'){
                    ++index;
                    double get_re=__get_data(str);
                    if(get_re==0.0){
                        //错误处理
                        error=true;
                    }else {
                        re/=(-get_re);
                    }
                }else {
                    double get_re=__get_data(str);
                    if(get_re==0.0){
                        //错误处理
                        error=true;
                    }else {
                        re/=(get_re);
                    }
                }
            }
        }
        return re;
    }
}


/***********************************************/
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView data;
    private TextView result;
    private String expression="";
    //等号开关
    private boolean sign_equal=false;
    //平方根开关
    private boolean sign_sqrt=false;
    //操作符开关
    private boolean sign_op=false;
    //小数点开关
    private boolean sign_point=false;
    private String result_value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data=findViewById(R.id.data);
        result=findViewById(R.id.result);
        init_button();
    }
    private void init_button(){
        Button one=findViewById(R.id.one);
        one.setOnClickListener(this);
        Button two=findViewById(R.id.two);
        two.setOnClickListener(this);
        Button three=findViewById(R.id.three);
        three.setOnClickListener(this);
        Button four=findViewById(R.id.four);
        four.setOnClickListener(this);
        Button five=findViewById(R.id.five);
        five.setOnClickListener(this);
        Button six=findViewById(R.id.six);
        six.setOnClickListener(this);
        Button seven=findViewById(R.id.seven);
        seven.setOnClickListener(this);
        Button eight=findViewById(R.id.eight);
        eight.setOnClickListener(this);
        Button nine=findViewById(R.id.nine);
        nine.setOnClickListener(this);
        Button zero=findViewById(R.id.zero);
        zero.setOnClickListener(this);
        Button sqrt=findViewById(R.id.sqrt);
        sqrt.setOnClickListener(this);
        Button mul=findViewById(R.id.mul);
        mul.setOnClickListener(this);
        Button div=findViewById(R.id.div);
        div.setOnClickListener(this);
        Button delete=findViewById(R.id.delete);
        delete.setOnClickListener(this);
        Button add=findViewById(R.id.add);
        add.setOnClickListener(this);
        Button sub=findViewById(R.id.sub);
        sub.setOnClickListener(this);
        Button equal=findViewById(R.id.equal);
        equal.setOnClickListener(this);
        Button point=findViewById(R.id.point);
        point.setOnClickListener(this);
        Button AC=findViewById(R.id.ac);
        AC.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        int len=expression.length();
        String temp= (String) ((Button)view).getText();
        //等号处理
        if(temp.equals("DE")){
            if(len>0){
                if(expression.charAt(len-1)=='￣'&&expression.charAt(len-2)=='√'){
                    expression=expression.substring(0,len-2);
                    sign_sqrt=false;
                }else
                {
                    sign_point= expression.charAt(len - 1) != '.';
                    expression=expression.substring(0,expression.length()-1);
                }
                if(expression.length()>=1){
                    char end_ch=expression.charAt(expression.length()-1);
                    sign_op= !(end_ch>='0'&&end_ch<='9');
                }
            }
            sign_equal=false;
        }
        else if(temp.equals("AC")){
            //清空
            expression="";
            result_value="";
            //开关控制
            //等号开关
            sign_equal=false;
            //平方根开关
            sign_sqrt=false;
            //操作符开关
            sign_op=false;
            //小数点开关
            sign_point=false;
        }
        else if(len>=80){
            Toast.makeText(MainActivity.this,"输入超过限长",Toast.LENGTH_SHORT).show();
        }
        else if(temp.equals("=")){
            if(!result_value.isEmpty()){
                if(result_value.equals("ERROR")){
                    Toast.makeText(MainActivity.this,"表达式错误",Toast.LENGTH_SHORT).show();
                }else {
                    expression=result_value;
                    result_value="";
                    sign_equal=true;
                    sign_op=false;
                    sign_point=true;
                    sign_sqrt=false;
                    adjust(expression.length(),data);
                    adjust(result_value.length(),result);
                }
            }
        }
        else
        //数字处理
        if(is_number(temp.charAt(0))){
            //等号是否开了
            expression=sign_equal?temp:expression+temp;
            if(sign_equal){
                sign_point=false;
            }
            sign_sqrt=true;
            sign_equal=false;
            sign_op=false;
        }else
        if(temp.charAt(0)=='-'){
            if(len>=1&&expression.charAt(len-1)!='-'){
                expression+=temp;
            }else if(expression.isEmpty()){
                expression=temp;
            }
            sign_op=true;
            sign_point=false;
            sign_equal=false;
            sign_sqrt=false;
        }else if(temp.charAt(0)=='√'){
            if(sign_equal){
                expression=temp;
            }else if(!sign_sqrt){
                expression+=temp;
            }
            sign_op=false;
            sign_point=false;
            sign_sqrt=true;
            sign_equal=false;
        }
        else if(temp.charAt(0)=='.'){
            if((!sign_point&&!sign_op)){
                expression+=temp;
                sign_point=true;
                sign_equal=false;
            }
        } else {
            if(!sign_op&&!expression.isEmpty()){
                expression+=temp;
                sign_sqrt=false;
                sign_op=true;
                sign_point=false;
                sign_equal=false;
            }
        }
        double parse_re=new Parse().parse(expression);
        if(Parse.error){
            Parse.error=false;
            Toast.makeText(MainActivity.this,"表达式有误",Toast.LENGTH_SHORT).show();
            result_value="ERROR";
        }else {
            result_value=sign_equal?"":""+parse_re;
        }
        adjust(expression.length(),data);
        adjust(result_value.length(),result);
        data.setText(expression);
        result.setText(result_value);
    }
    private boolean is_number(char ch){
        return ch >= '0' && ch <= '9';
    }
    private void adjust(int len,TextView view){
        if(len>10){
            view.setTextSize(25);
        }
        if(len<=10){
            view.setTextSize(50);
        }
    }
}