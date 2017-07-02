package com.alberto.videoteca;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Alberto on 02/07/2017.
 */

public class ActividadProtected extends Activity {

    public static final String MOVIE = "Movie";
    public static final String SHARED_ELEMENT_NAME = "hero";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password);
        final SharedPreferences sharedPref = getPreferences(getApplicationContext().MODE_PRIVATE);
        findViewById(R.id.accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passSaved = sharedPref.getString("password", "");
                String passIntroduce = ((EditText)findViewById(R.id.pass)).getText().toString();
                if(passSaved == ""){
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("password", passIntroduce);
                    editor.commit();
                    returnActivity();
                }else{
                    if( passSaved.equalsIgnoreCase(passIntroduce)){
                        returnActivity();
                    }else{
                        ((TextView)findViewById(R.id.error)).setText("Contraseña incorrecta");
                        ((TextView)findViewById(R.id.error)).setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void returnActivity() {
        Intent intent = new Intent();
        setResult(1, intent);
        finish();
    }
}
