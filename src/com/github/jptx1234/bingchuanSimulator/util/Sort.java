package com.github.jptx1234.bingchuanSimulator.util;

import java.util.HashSet;

public class Sort {

	static Integer[] arr = null;
	static int num = 1;
	public static void main(String[] args) {
//		String s = "542f,$52a8,$65f6,$81ea,$52a8,$8ba4,$8bc1,$914d,$7f6e,$6587,$4ef6,$8bfb,$53d6,$6d41,$5173,$95ed,$5931,$8d25,$672c,$6587,$4ef6,$662f,$7684,$914d,$7f6e,$6587,$4ef6,$5220,$9664,$672c,$6587,$4ef6,$4f1a,$5bfc,$81f4,$7684,$8bbe,$7f6e,$88ab,$6e05,$7a7a,$5bc6,$7801,$662f,$52a0,$5bc6,$8fc7,$7684,$8bf7,$4e0d,$8981,$66f4,$6539,$628a,$672c,$6587,$4ef6,$653e,$5230,$548c,$7a0b,$5e8f,$540c,$76ee,$5f55,$4e0b,$5373,$53ef,$88ab,$81ea,$52a8,$52a0,$8f7d,$672c,$6587,$4ef6,$53ef,$8de8,$5e73,$53f0,$4f7f,$7528,$8d26,$6237,$5bc6,$7801,$914d,$7f6e,$6587,$4ef6,$521b,$5efa,$5931,$8d25,$914d,$7f6e,$6587,$4ef6,$5df2,$521b,$5efa,$4f46,$8f93,$51fa,$6d41,$5931,$8d25,$672a,$53d1,$73b0,$914d,$7f6e,$6587,$4ef6,$5373,$5c06,$8f6c,$5230,$8bbe,$7f6e,$9762,$677f,$914d,$7f6e,$6587,$4ef6,$8f93,$51fa,$6d41,$5173,$95ed,$5931,$8d25,$8bf7,$70b9,$51fb,$4e0a,$7ebf,$8d26,$6237,$8d26,$6237,$8d26,$6237,$5bc6,$7801,$5bc6,$7801,$4e0a,$7ebf,$8bbe,$7f6e,$8bf7,$70b9,$51fb,$4e0a,$7ebf,$7a0b,$5e8f,$5df2,$6700,$5c0f,$5316,$8def,$7531,$5668,$8ba4,$8bc1,$8def,$7531,$5668,$7aef,$53e3,$5730,$5740,$8def,$7531,$5668,$8def,$7531,$5668,$7aef,$53e3,$5730,$5740,$8d26,$6237,$5bc6,$7801,$8def,$7531,$5668,$8def,$7531,$5668,$8ba4,$8bc1,$6210,$529f,$540e,$6700,$5c0f,$5316,$8ba4,$8bc1,$5fc3,$8df3,$5ef6,$65f6,$542f,$52a8,$65f6,$81ea,$52a8,$8ba4,$8bc1,$8ba4,$8bc1,$65f6,$6dfb,$52a0,$8def,$7531,$8def,$7531,$5668,$683c,$5f0f,$8ba4,$8bc1,$9009,$9879,$8ba4,$8bc1,$5fc3,$8df3,$5305,$95f4,$9694,$8ba4,$8bc1,$5fc3,$8df3,$5ef6,$65f6,$5efa,$8bae,$5c0f,$4e8e,$79d2,$79d2,$8f6f,$4ef6,$542f,$52a8,$65f6,$81ea,$52a8,$8ba4,$8bc1,$542f,$52a8,$65f6,$81ea,$52a8,$8ba4,$8bc1,$8ba4,$8bc1,$65f6,$81ea,$52a8,$6dfb,$52a0,$8def,$7531,$53ef,$89e3,$51b3,$4e0b,$65e0,$6cd5,$8ba4,$8bc1,$7684,$95ee,$9898,$8ba4,$8bc1,$65f6,$6dfb,$52a0,$8def,$7531,$786e,$5b9a,$53d6,$6d88,$663e,$793a,$7a97,$53e3,$4e0a,$7ebf,$9000,$51fa,$6dfb,$52a0,$6258,$76d8,$56fe,$6807,$5931,$8d25,$8d26,$6237,$4e0d,$80fd,$4e3a,$7a7a,$5bc6,$7801,$4e0d,$80fd,$4e3a,$7a7a,$8def,$7531,$5668,$8def,$7531,$5668,$624b,$52a8,$586b,$5199,$653e,$5f03,$8ba4,$8bc1,$8bd5,$56fe,$901a,$8fc7,$89e3,$6790,$9ed8,$8ba4,$8def,$7531,$83b7,$53d6,$672c,$673a,$548c,$5931,$8d25,$662f,$5426,$624b,$52a8,$586b,$5199,$8ba4,$8bc1,$6240,$9700,$548c,$624b,$52a8,$586b,$5199,$4fdd,$6301,$73b0,$72b6,$60a8,$7684,$4e3a,$53ef,$80fd,$6b63,$5728,$901a,$8fc7,$8def,$7531,$5668,$8fdb,$884c,$8ba4,$8bc1,$662f,$5426,$624b,$52a8,$586b,$5199,$8ba4,$8bc1,$6240,$9700,$548c,$4e0b,$7ebf,$4e0b,$7ebf,$4e0a,$7ebf,$5df2,$505c,$6b62,$8ba4,$8bc1,$4e0a,$7ebf,$683c,$5f0f,$4e0d,$6b63,$786e,$683c,$5f0f,$793a,$4f8b,$683c,$5f0f,$4e0d,$6b63,$786e,$683c,$5f0f,$793a,$4f8b,$8def,$7531,$5668,$8def,$7531,$5668,$8ba4,$8bc1,$5fc3,$8df3,$5ef6,$65f6,$542f,$52a8,$65f6,$81ea,$52a8,$8ba4,$8bc1,$8ba4,$8bc1,$65f6,$6dfb,$52a0,$8def,$7531,$8ba4,$8bc1,$65f6,$6dfb,$52a0,$8def,$7531,$914d,$7f6e,$6587,$4ef6,$8bfb,$53d6,$5931,$8d25,$5f88,$62b1,$6b49,$51fa,$4e8e,$4e0d,$5f97,$5df2,$7684,$539f,$56e0,$6211,$5fc5,$987b,$6682,$505c,$6b64,$9879,$76ee,$5177,$4f53,$5f00,$653e,$65f6,$95f4,$8bf7,$81ea,$884c,$901a,$8fc7,$76f8,$5173,$6e20,$9053,$83b7,$5f97,$4e0e,$8ba4,$8bc1,$670d,$52a1,$5668,$8fde,$63a5,$5931,$8d25,$8fd0,$6b63,$5728,$542f,$52a8,$8ba4,$8bc1,$7ebf,$7a0b,$4e0e,$8ba4,$8bc1,$670d,$52a1,$5668,$8fde,$63a5,$51fa,$9519";
		String s = toUnicodeString("启动时自动认证配置文件读取流关闭失败本文件是的配置文件删除本文件会导致的设置被清空密码是加密过的请不要更改把本文件放到和程序同目录下即可被自动加载本文件可跨平台使用账户密码配置文件创建失败配置文件已创建但输出流失败未发现配置文件即将转到设置面板配置文件输出流关闭失败请点击上线账户账户账户密码密码上线设置请点击上线程序已�?小化路由器认证路由器端口地址路由器路由器端口地址账户密码路由器路由器认证成功后最小化认证心跳延时启动时自动认证认证时添加路由路由器格式认证�?�项认证心跳包间隔认证心跳延时建议小于秒秒软件启动时自动认证启动时自动认证认证时自动添加路由可解决下无法认证的问题认证时添加路由确定取消显示窗口上线�?出添加托盘图标失败账户不能为空密码不能为空路由器路由器手动填写放弃认证试图�?�过解析默认路由获取本机和失败是否手动填写认证所�?和手动填写保持现状您的为可能正在通过路由器进行认证是否手动填写认证所�?和下线下线上线已停止认证上线格式不正确格式示例格式不正确格式示例路由器路由器认证心跳延时启动时自动认证认证时添加路由认证时添加路由配置文件读取失败很抱歉出于不得已的原因我必须暂停此项目具体�?放时间请自行通过相关渠道获得再见运与认证服务器连接失败与认证服务器连接出�?");
		String[] ss = s.split(",\\$");
		HashSet<Integer> set = new HashSet<>();
		for (int i = 0; i < ss.length; i++) {
			set.add(Integer.valueOf(ss[i], 16));
		}
		arr = new Integer[set.size()];
		arr = set.toArray(arr);
		sor(0, arr.length-1);
		StringBuilder sb = new StringBuilder();
		int start = Integer.valueOf("4e00",16);
		int end = Integer.valueOf("9fa5", 16);
		int key = start;
		for (Integer integer : arr) {
			sb.append(getRa(key, integer-1));
			key = integer +1;
		}
		sb.append(getRa(key, end));
		System.out.println(sb);
	}

	static String toUnicodeString(String s) {
	       StringBuffer sb = new StringBuffer();
	       for (int i = 0; i < s.length(); i++) {
	         char c = s.charAt(i);
	         if (c >= 0 && c <= 255) {
	           sb.append(c);
	         }
	         else {
	          sb.append(",$"+Integer.toHexString(c));
	         }
	       }
	       return sb.toString().replaceFirst(",\\$", "");
	     }

	static int t = 1;
	static String getRa(int from,int to){
		StringBuilder sb = new StringBuilder();
		if (from > to) {
			return "";
		}else if (from == to) {
			sb.append(hex(from));
		}else {
			sb.append(hex(from));
			sb.append("-");
			sb.append(hex(to));
		}
		String en;
		if (t++ > 100) {
			t = 1;
			en = "\n";
		}else {
			en = ",";
		}
		sb.append(en);
		return sb.toString();
	}

	static String hex(int i){
		return "$"+Integer.toString(i,16);
	}

	static void sor(int low, int high){
		if (low >= high) {
			return;
		}
		int temp;
		int key = low;
		int start = low;
		int end = high;
		while (low < high) {
			while (arr[high] >= arr[key] && high > low) {
				high--;
			}
			if (high > low) {
				temp = arr[high];
				arr[high] = arr[key];
				arr[key] = temp;
				key = high;
			}
			while (arr[low] < arr[key] && low < high) {
				low++;
			}
			if (high > low) {
				temp = arr[low];
				arr[low] = arr[key];
				arr[key] = temp;
				key = low;
			}
		}
		sor(start, key-1);
		sor(key+1, end);
	}

}
