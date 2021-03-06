/*
 * This software is released under the BSD license. For full license see License-library.txt file.
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine.android.example;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.leanengine.LeanEntity;
import com.leanengine.LeanError;
import com.leanengine.NetworkCallback;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;


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
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button loginButton = (Button) findViewById(R.id.startButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ViewActivity.this.getListView()
                        .setAdapter(new MyCustomBaseAdapter(ViewActivity.this, new LeanEntity[0]));
                loadEntities();
            }
        });
    }

    private void loadEntities() {
        LeanEntity.getAllInBackground(null, new NetworkCallback<LeanEntity>() {

            @Override
            public void onResult(LeanEntity... result) {
                ViewActivity.this.getListView().setAdapter(new MyCustomBaseAdapter(ViewActivity.this, result));
                ViewActivity.this.getListView().setTextFilterEnabled(true);
            }

            @Override
            public void onFailure(final LeanError error) {
                Toast.makeText(ViewActivity.this, "Error invoking REST service.", 2000).show();
            }
        });

    }

    private static class MyCustomBaseAdapter extends BaseAdapter {
        private static LeanEntity[] entities;
        private LayoutInflater mInflater;

        private MyCustomBaseAdapter(Context context, LeanEntity... results) {
            entities = results;
            mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return entities.length;
        }

        public Object getItem(int i) {
            return entities[i];
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

            holder.txtName.setText(entities[i].getKind());
            holder.txtProperties.setText(getPropertiesString(entities[i]));

            return view;
        }

        private String getPropertiesString(LeanEntity entity) {
            if (entity.getKeySet() == null) {
                return "";
            } else {
                StringBuilder builder = new StringBuilder();
                Set<String> keySet = entity.getKeySet();
                for (String key : keySet) {
                    builder.append(key).append(": ").append(entity.get(key).toString()).append(" | ");
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