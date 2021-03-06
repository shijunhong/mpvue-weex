/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.taobao.weex.dom.action;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.DOMAction;
import com.taobao.weex.dom.DOMActionContext;
import com.taobao.weex.utils.FontDO;
import com.taobao.weex.utils.TypefaceUtil;

/**
 * Created by sospartan on 02/03/2017.
 */
public final class AddRuleAction implements DOMAction {
  private final String mType;
  private final JSONObject mData;

  public AddRuleAction(String type, JSONObject data) {
    this.mType = type;
    this.mData = data;
  }

  @Override
  public void executeDom(DOMActionContext context) {
    if (Constants.Name.FONT_FACE.equals(mType)) {
      FontDO fontDO = parseFontDO(mData, context.getInstance());
      if (fontDO != null && !TextUtils.isEmpty(fontDO.getFontFamilyName())) {
        FontDO cacheFontDO = TypefaceUtil.getFontDO(fontDO.getFontFamilyName());
        if (cacheFontDO == null || !TextUtils.equals(cacheFontDO.getUrl(), fontDO.getUrl())) {
          TypefaceUtil.putFontDO(fontDO);
          TypefaceUtil.loadTypeface(fontDO);
        } else {
          TypefaceUtil.loadTypeface(cacheFontDO);
        }
      }
    }
  }

  private FontDO parseFontDO(JSONObject jsonObject,WXSDKInstance instance) {
    if(jsonObject == null) {
      return null;
    }
    String src = jsonObject.getString(Constants.Name.SRC);
    String name = jsonObject.getString(Constants.Name.FONT_FAMILY);

    return new FontDO(name, src,instance);
  }
}
