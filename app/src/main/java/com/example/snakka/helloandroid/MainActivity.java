package com.example.snakka.helloandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener{

    private static final int REQUEST_CODE_ANOTHER_CALC_1 = 1;
    private static final int REQUEST_CODE_ANOTHER_CALC_2 = 2;

    private EditText numInput1;
    private EditText numInput2;
    private Spinner operatorSelector;

    private TextView calcResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //このActivityで使われるlayoutファイルを指定する

        /*ボタンのリスナークラスを登録*/
        findViewById(R.id.calcButton1).setOnClickListener(this);
        findViewById(R.id.calcButton2).setOnClickListener(this);
        findViewById(R.id.nextCalc).setOnClickListener(this);


        /*文字入力イベント受け取り*/
        numInput1 = (EditText) findViewById(R.id.numInput1);
        numInput1.addTextChangedListener(this);

        numInput2 = (EditText)findViewById(R.id.numInput2);
        numInput2.addTextChangedListener(this);


        operatorSelector = (Spinner)findViewById(R.id.operatorSelector);
        calcResult = (TextView)findViewById(R.id.calcResult);

    }


    @Override //テキストが変更される直前に呼ばれる。sは変更前の内容
    public void beforeTextChanged(CharSequence s, int start, int count, int after){

    }

    @Override //テキストが変更されるときに呼ばれる。sは変更後の内容で編集不可能
    public void onTextChanged(CharSequence s, int start, int before, int count){

    }

    @Override //テキストが変更された後に呼ばれる。sは変更後の内容で編集可能
    public void afterTextChanged(Editable s){
        refreshResult();
    }


    @Override
    public void onClick(View v){
        int buttonId = v.getId(); //押されたボタンのIDを取得

        switch(buttonId){
            case R.id.calcButton1:
                Intent intent1 = new Intent(this, AnotherCalcActivity.class);
                startActivityForResult(intent1, REQUEST_CODE_ANOTHER_CALC_1);
                break;
            case R.id.calcButton2:
                Intent intent2 = new Intent(this, AnotherCalcActivity.class);
                startActivityForResult(intent2, REQUEST_CODE_ANOTHER_CALC_2);
                break;
            case R.id.nextCalc:
                if(!checkEditTextInput()) return;

                long result = calc();
                numInput1.setText(String.valueOf(result));
                refreshResult();
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK) return;

        Bundle resultBundle = data.getExtras();
        if(!resultBundle.containsKey("result")) return;
        int result = resultBundle.getInt("result");


        if(requestCode == REQUEST_CODE_ANOTHER_CALC_1){
            numInput1.setText(String.valueOf(result));
        }else if(requestCode == REQUEST_CODE_ANOTHER_CALC_2){
            numInput2.setText(String.valueOf(result));
        }

        refreshResult();
    }



    //EditTextの編集が終わった後に呼ばれ、場合によってresultに反映する
    private void refreshResult(){
        if(checkEditTextInput()){ //2項目に値が書き込まれているか?
            long result = calc();

            String resultText = getString(R.string.calcResult, result); //resultの数値が、calcResultの書式になって返る
            calcResult.setText(resultText);
        }else{ //両方の入力が終了していない場合、計算結果をデフォルトに戻す
            calcResult.setText(R.string.calcResultDefault);
        }
    }

    private boolean checkEditTextInput(){
        String input1 = numInput1.getText().toString();
        String input2 = numInput2.getText().toString();

        return !TextUtils.isEmpty(input1) && !TextUtils.isEmpty(input2);
    }


    private long calc(){
        String input1 = numInput1.getText().toString();
        String input2 = numInput2.getText().toString();


        long num1, num2;
        try{
            num1 = Long.parseLong(input1);
            num2 = Long.parseLong(input2);
        }catch(NumberFormatException e){ //入力された数字がlong型の範囲を超えた場合、0初期化
            num1 = 0;
            num2 = 0;
        }

        //Spinnerから、選択中のindexを取得する
        int operator = operatorSelector.getSelectedItemPosition();

        switch(operator){
            case 0:
                return num1 + num2;
            case 1:
                return num1 - num2;
            case 2:
                return num1 * num2;
            case 3:
                return num1 / num2;
            default:
                throw new RuntimeException();
        }
    }

}
