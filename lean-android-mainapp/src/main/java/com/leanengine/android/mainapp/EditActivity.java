package com.leanengine.android.mainapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.leanengine.LeanEntity;
import com.leanengine.LeanError;
import com.leanengine.rest.NetworkCallback;
import com.leanengine.rest.RestService;

import java.util.HashMap;


public class EditActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);
        final EditText entityName = (EditText) findViewById(R.id.entityName);
        final EditText key1 = (EditText) findViewById(R.id.propertyKey1);
        final EditText val1 = (EditText) findViewById(R.id.propertyValue1);
        final EditText key2 = (EditText) findViewById(R.id.propertyKey2);
        final EditText val2 = (EditText) findViewById(R.id.propertyValue2);
        final EditText key3 = (EditText) findViewById(R.id.propertyKey3);
        final EditText val3 = (EditText) findViewById(R.id.propertyValue3);

        Button loginButton = (Button) findViewById(R.id.saveButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                LeanEntity entity = new LeanEntity();
                entity.kind = entityName.getText().toString();
                entity.properties = new HashMap<String, Object>();
                String k1 = key1.getText().toString();
                String v1 = val1.getText().toString();
                if (k1 != null && k1.length() != 0 && v1 != null && v1.length() != 0) {
                    entity.properties.put(k1, v1);
                }
                String k2 = key2.getText().toString();
                String v2 = val2.getText().toString();
                if (k2 != null && k2.length() != 0 && v2 != null && v2.length() != 0) {
                    entity.properties.put(k2, v2);
                }
                String k3 = key3.getText().toString();
                String v3 = val3.getText().toString();
                if (k3 != null && k3.length() != 0 && v3 != null && v3.length() != 0) {
                    entity.properties.put(k3, v3);
                }

                if(entity.properties.isEmpty()){
                    return;
                }
                
                RestService.putPrivateEntity(entity, new NetworkCallback<Long>() {
                    @Override
                    public void onResult(Long... result) {
                        Toast.makeText(EditActivity.this, "Entity saved.", 2000).show();
                        entityName.getText().clear();
                        key1.getText().clear();
                        val1.getText().clear();
                        key2.getText().clear();
                        val2.getText().clear();
                        key3.getText().clear();
                        val3.getText().clear();
                    }

                    @Override
                    public void onFailure(LeanError error) {
                        Toast.makeText(EditActivity.this, "Error invoking REST service.", 2000).show();
                    }
                });
            }
        });
    }
}