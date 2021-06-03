package com.ew.jsontree.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.ew.jsontree.utils.JSONObjectKeeped;
import org.json.JSONException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by WYM on 2016/7/23.
 */
public class JsonTreeView extends LinearLayout {
    private LinearLayout rootContainer;
    private int level = 0;
    private int maxLevel;//设置jsontree显示的最深级别，不能超过20级，否则容易引发stackOverFlowError

    public JsonTreeView(Context context) {
        super(context);
    }

    public JsonTreeView(Context context, Map<String, Object> jsonMap, int maxLevel) {
        super(context);
        this.maxLevel = maxLevel <= 20 ? maxLevel : 20;
        rootContainer = new LinearLayout(getContext());
        rootContainer.setLayoutParams(new LayoutParams(-1, -1));
        rootContainer.setOrientation(VERTICAL);
        rootContainer.setBackgroundColor(Color.WHITE);
        addView(rootContainer);
        createTreeViewOfKeeepedOfOptimize(rootContainer, (LinkedHashMap<String, Object>) jsonMap);
    }

    public JsonTreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 创建的json tree view中节点顺序与输入的json string保持一致
     *
     * @param rootView
     * @param jsonMap
     */
    public void createTreeViewOfKeeepedOfOptimize(LinearLayout rootView, LinkedHashMap<String, Object> jsonMap, Boolean... isVirtualNode) {
        level += 1;
        if (level > maxLevel) {
            return;
        }
        for (LinkedHashMap.Entry<String, Object> entry : jsonMap.entrySet()) {
            TreeItemView view = new TreeItemView(getContext());
            LinearLayout.LayoutParams lp = new LayoutParams(-2, -2);
            lp.setMargins(50, 0, 0, 0);
            rootView.addView(view, lp);
            String size = null, value = null, btnText = null;
            boolean isExpend = level <= 4 ? true : false;
            String key = entry.getKey();
            Object obj = entry.getValue();
            if (obj instanceof JSONObjectKeeped) {
                LinkedHashMap<String, Object> map = ((JSONObjectKeeped) obj).getMap();
                size = "{" + String.valueOf(map.size()) + "}";
                if (map.size() == 0) {
                    btnText = "";
                    isExpend = false;
                }
                view.setData(key, size, value, btnText, isVirtualNode != null && isVirtualNode.length == 1 && isVirtualNode[0], isExpend);
                //创建子节点的tree
                createTreeViewOfKeeepedOfOptimize(view, map);
            } else if (obj instanceof org.json.JSONArray) {
                org.json.JSONArray jsonArray = (org.json.JSONArray) obj;
                size = "[" + String.valueOf(jsonArray.length()) + "]";
                if (jsonArray.length() == 0) {
                    btnText = "";
                    isExpend = false;
                }
                view.setData(key + level, size, value, btnText, isVirtualNode != null && isVirtualNode.length == 1 && isVirtualNode[0], isExpend);
                for (int i = 0; i < (jsonArray).length(); i++) {
                    Object each = null;
                    try {
                        each = jsonArray.get(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (each != null) {
                        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                        map.put(String.valueOf(i), each);
                        //创建子节点的tree
                        createTreeViewOfKeeepedOfOptimize(view, map, true);
                    }
                }
            } else {  //基础数据类型
                value = String.valueOf(obj);
                view.setData(key + level, size, value, btnText, isVirtualNode != null && isVirtualNode.length == 1 && isVirtualNode[0], isExpend);
            }
        }
        level -= 1;
    }
}
