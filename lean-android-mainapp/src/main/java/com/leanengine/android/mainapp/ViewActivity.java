package com.leanengine.android.mainapp;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.leanengine.LeanEntity;
import com.leanengine.rest.NetworkCallback;
import com.leanengine.rest.RestException;
import com.leanengine.rest.RestService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewActivity extends ListActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_layout);

        ListView lv = getListView();
        loadEntities();

//        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                return false;  //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });
//
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                // When clicked, show a toast with the TextView text
//                Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
//                        Toast.LENGTH_SHORT).show();
//            }
//        });

        Button loginButton = (Button) findViewById(R.id.loadButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                        ViewActivity.this.getListView()
                        .setAdapter(new MyCustomBaseAdapter(ViewActivity.this, new ArrayList<LeanEntity>(0)));
                loadEntities();
            }
        });
    }

    private void loadEntities() {
        RestService.getPrivateEntitiesAsync(new NetworkCallback<LeanEntity>() {
            @Override
            public void onResult(List<LeanEntity> result) {
                ViewActivity.this.getListView()
                        .setAdapter(new MyCustomBaseAdapter(ViewActivity.this, result));
                ViewActivity.this.getListView().setTextFilterEnabled(true);
            }

            @Override
            public void onResult(LeanEntity result) {
                // do nothing here - we load a List of entities
                Log.e("SOMETHING", "MORE");
            }

            @Override
            public void onFailure(final RestException restException) {
                Toast.makeText(ViewActivity.this, "Error invoking REST service.", 2000).show();
            }
        });

    }

    private ArrayList<FakeEntity> getFakeEntities() {
        ArrayList<FakeEntity> fakeEntities = new ArrayList<FakeEntity>();

        Map<String, Object> fakePropertValueMap1 = new HashMap<String, Object>();
        fakePropertValueMap1.put("fist_name", "Austin");
        fakePropertValueMap1.put("last_name", "Powers");
        fakePropertValueMap1.put("age", 40);
        fakeEntities.add(new FakeEntity("Person", fakePropertValueMap1));

        Map<String, Object> fakePropertValueMap2 = new HashMap<String, Object>();
        fakePropertValueMap2.put("fist_name", "Scott");
        fakePropertValueMap2.put("last_name", "Evil");
        fakePropertValueMap2.put("age", 16);
        fakeEntities.add(new FakeEntity("Person", fakePropertValueMap2));

        Map<String, Object> fakePropertValueMap3 = new HashMap<String, Object>();
        fakePropertValueMap3.put("fist_name", "Vanessa");
        fakePropertValueMap3.put("last_name", "Kensington");
        fakePropertValueMap3.put("age", 27);
        fakeEntities.add(new FakeEntity("Person", fakePropertValueMap3));
        return fakeEntities;
    }

    private static class MyCustomBaseAdapter extends BaseAdapter {
        private static List<LeanEntity> entities;
        private LayoutInflater mInflater;

        private MyCustomBaseAdapter(Context context, List<LeanEntity> results) {
            entities = results;
            mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return entities.size();
        }

        public Object getItem(int i) {
            return entities.get(i);
        }

        public long getItemId(int i) {
            return i;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                view = mInflater.inflate(R.layout.entity_row_view, null);
                holder = new ViewHolder();
                holder.txtName = (TextView) view.findViewById(R.id.name);
                holder.txtProperties = (TextView) view.findViewById(R.id.properties);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.txtName.setText(entities.get(i).kind);
            holder.txtProperties.setText(getPropertiesString(entities.get(i)));

            return view;
        }

        private String getPropertiesString(LeanEntity entity) {
            if (entity.properties.isEmpty()) {
                return "";
            } else {
                StringBuilder builder = new StringBuilder();

                for (Map.Entry<String, Object> entry : entity.properties.entrySet()) {
                    builder.append(entry.getKey()).append(": ").append(entry.getValue().toString()).append(" | ");
                }
                return builder.substring(0, builder.length() - 2);
            }
        }

        private static class ViewHolder {
            TextView txtName;
            TextView txtProperties;
        }
    }
}