package com.ew.jsontree.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.ew.jsontree.utils.JSONObjectKeeped;
import org.json.JSONException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * json tree view的改进版本
 * 支持的json层级没有限制，理论上支持无限层级
 * Created by WYM on 2016/8/1.
 */

public class AdvancedJsonTreeView extends LinearLayout {

    private int level = 0;
    private LinearLayout rootContainer;

    public AdvancedJsonTreeView(Context context, Map<String, Object> jsonMap) {
        super(context);
        rootContainer = (LinearLayout) getRootView();
        rootContainer.setOrientation(VERTICAL);
        createTreeViewOfKeepedOptimize((LinkedHashMap<String, Object>) jsonMap, true);
    }

    public AdvancedJsonTreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void createTreeViewOfKeepedOptimize(LinkedHashMap<String, Object> jsonMap, Boolean... isVirtualNode) {
        level += 1;
        for (LinkedHashMap.Entry<String, Object> entry : jsonMap.entrySet()) {
            final AdvancedTreeItemView itemView = new AdvancedTreeItemView(getContext());
            itemView.setTag(level);
            LinearLayout.LayoutParams lp = new LayoutParams(-2, -2);
            lp.setMargins(level * 40, 0, 0, 0);
            rootContainer.addView(itemView, lp);
            itemView.setBtnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = ((Button) v).getText().toString();
                    if (str.equals("+")) {
                        //进行展开
                    } else {
                        //进行折叠
                    }
                    itemView.btnExpend.setText(itemView.btnExpend.getText().equals("-") ? "+" : "-");

                    int currtLevelTag = Integer.parseInt(itemView.getTag().toString());
                    boolean startSearchChildNodes = false;
                    //查找当前节点的子节点，然后设置其visibility
                    for (int j = 0; j < rootContainer.getChildCount(); j++) {
                        AdvancedTreeItemView childView = (AdvancedTreeItemView) rootContainer.getChildAt(j);
                        if (startSearchChildNodes) {            //开始查找其子节点
                            int levelTag = Integer.valueOf(childView.getTag().toString());
                            if (levelTag > currtLevelTag) {       //子节点开始
                                /*if(str.equals("-")){
                                    //进行折叠，保存子节点的当前的状态
                                    childView.setExpendState(childView.btnExpend.getText().toString().equals("+") ? GONE : VISIBLE);
                                }*/
                                if (itemView.btnExpend.getText().equals("-")) {
                                    AdvancedTreeItemView currtParent = (AdvancedTreeItemView) rootContainer.getChildAt(getParentNodeIndex(rootContainer, levelTag, j));//找到其父节点，根据其父节点btn判断是否展开
                                    Button btn = currtParent.btnExpend;
                                    childView.setVisibility(btn.getText().equals("-") ? VISIBLE : GONE);
                                } else {
                                    childView.setVisibility(GONE);
                                }
                            } else {                            //子节点结束
                                break;
                            }
                        } else {
                            if (childView.equals(itemView)) {  //找到当前节点，下一个节点则为其子节点
                                startSearchChildNodes = true;
                            }
                        }
                    }
                }
            });

            String key, size = null, btnText = "-";
            Object value = null;
            Boolean isExpend = false;
            if (level < 4) {
                isExpend = true;
//                btnText = "-";
            }
            key = entry.getKey();

            Object obj = entry.getValue();
            if (obj instanceof JSONObjectKeeped) {
                LinkedHashMap<String, Object> map = ((JSONObjectKeeped) obj).getMap();
                size = "{" + String.valueOf(map.size()) + "}";
                if (map.size() == 0) {
                    btnText = "";
                }
                //创建当前节点
                itemView.setData(key, size, value, btnText, isVirtualNode != null && isVirtualNode.length == 1 && isVirtualNode[0], isExpend);
                //创建子节点的tree
                createTreeViewOfKeepedOptimize(map);
            } else if (obj instanceof org.json.JSONArray) {
                org.json.JSONArray jsonArray = (org.json.JSONArray) obj;
                size = "[" + String.valueOf(jsonArray.length()) + "]";
                if (jsonArray.length() == 0) {
                    btnText = "";
                }
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
                        //创建当前节点
                        itemView.setData(key, size, value, btnText, isVirtualNode != null && isVirtualNode.length == 1 && isVirtualNode[0], isExpend);
                        //创建子节点的tree
                        createTreeViewOfKeepedOptimize(map, true);
                    }
                }
            } else {  //基础数据类型
                //创建当前节点
                value = obj;
                itemView.setData(key, size, value, btnText, isVirtualNode != null && isVirtualNode.length == 1 && isVirtualNode[0], isExpend);
            }
        }
        level -= 1;
    }

    public int getParentNodeIndex(LinearLayout rootView, int currtLevel, int currtIndex) {
        for (int i = currtIndex; i >= 0; i--) {
            if (Integer.valueOf(rootView.getChildAt(i).getTag().toString()) < currtLevel) {
                return i;
            }
        }
        return 0;
    }
}
