

package com.remote.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author Mark sunlightcs@gmail.com
 */
public class R extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	public R() {
		put("code", 200);
		put("msg", "success");
	}

	public static R error() {
		return error(500, "未知异常，请联系管理员");
	}
	
	public static R error(String msg) {
		return error(500, msg);
	}
	
	public static R error(int code, String msg) {
		R r = new R();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static R ok(String msg) {
		R r = new R();
		r.put("msg", msg);
		return r;
	}
	
	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}

	public static R ok(Object obj) {
		R r = new R();
		r.put("data",obj);
		return r;
	}

	public static R ok() {
		return new R();
	}

	@Override
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
	public static R error(Object obj) {
		R r = new R();
		r.put("data",obj);
		return r;
	}
	public boolean isOK(){
		try {
			Integer code = (Integer)this.get("code");
			if(code == 200){
				return true;
			}else{
				return false;
			}
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
