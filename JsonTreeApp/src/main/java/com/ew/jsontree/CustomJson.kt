package com.ew.jsontree

import android.app.Activity
import android.os.Bundle
import android.widget.HorizontalScrollView
import com.ew.jsontree.utils.JsonFormatUtils
import com.ew.jsontree.view.AdvancedJsonTreeView

class CustomJson : Activity() {
    private lateinit var llRoot: HorizontalScrollView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_json_tree)
        llRoot = findViewById(R.id.ll_root_container)
        var json = "[\n" +
                "  {\n" +
                "    \"domain\": \".blogger.com\",\n" +
                "    \"httpOnly\": true,\n" +
                "    \"maxAge\": -1,\n" +
                "    \"name\": \"S\",\n" +
                "    \"path\": \"/\",\n" +
                "    \"secure\": true,\n" +
                "    \"toDiscard\": false,\n" +
                "    \"value\": \"blogger\\u003dAFujbtjcY3_nNTPpQjTTyDFIBhvzCOvMtuh9kka8DQ8\",\n" +
                "    \"version\": 0,\n" +
                "    \"whenCreated\": 1620643465535\n" +
                "  },\n" +
                "  {\n" +
                "    \"domain\": \".facebook.com\",\n" +
                "    \"httpOnly\": true,\n" +
                "    \"maxAge\": 7775998,\n" +
                "    \"name\": \"fr\",\n" +
                "    \"path\": \"/\",\n" +
                "    \"secure\": true,\n" +
                "    \"toDiscard\": false,\n" +
                "    \"value\": \"1REo4698dD3qmsCTa..Bgidsw.jM.AAA.0.0.Bgidyd.AWUIc4FW3Ug\",\n" +
                "    \"version\": 0,\n" +
                "    \"whenCreated\": 1619647645294\n" +
                "  }\n" +
                "]"

        val map = JsonFormatUtils.jsonToMapKeeped(json)

        val view = AdvancedJsonTreeView(this, map)
        llRoot.addView(view)
    }
}