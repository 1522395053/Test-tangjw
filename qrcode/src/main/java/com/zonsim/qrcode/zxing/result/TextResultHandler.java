/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zonsim.qrcode.zxing.result;

import android.app.Activity;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;

/**
 * This class handles TextParsedResult as well as unknown formats. It's the fallback handler.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class TextResultHandler extends ResultHandler {
	
	public TextResultHandler(Activity activity, ParsedResult result, Result rawResult) {
		super(activity, result, rawResult);
	}
	
}
