package com.ew.jsontree

import android.app.Activity
import android.os.Bundle
import android.widget.HorizontalScrollView
import com.ew.jsontree.R
import com.ew.jsontree.utils.JsonFormatUtils
import com.ew.jsontree.view.AdvancedJsonTreeView

class AdvancedJsonTreeActivity : Activity() {
    private lateinit var llRoot: HorizontalScrollView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_json_tree)
        llRoot = findViewById(R.id.ll_root_container)
        val json = """{
  "bstatus": {
    "code": 0,
    "des": "查询成功!"
  },
  "data": {
    "allFilters": [
      {
        "details": [
          {
            "detailId": "timeArea",
            "detailItems": [
              {
                "clearOthers": false,
                "detailItemId": "depTime",
                "detailItemTitle": "",
                "isDefaultItem": false,
                "locked": false,
                "selected": false,
                "timeArea": true,
                "value": "00:00;24:00"
              }
            ],
            "detailTitle": ""
          }
        ],
        "filterId": "time",
        "filterTitle": "起飞时段"
      }
     
    ]
  }
}"""
        val map = JsonFormatUtils.jsonToMapKeeped(json)
        val view = AdvancedJsonTreeView(this, map)
        llRoot.addView(view)
    }
}