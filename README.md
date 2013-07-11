FuturesBot - 台指期當沖自動交易機器人
===========
作者：Philipz(philipzheng@gmail.com)<br/>
網站：http://www.tradingbot.com.tw/<br/>
Facebook粉絲團：http://www.facebook.com/tradingbot<br/>
Blog：http://server.everfine.com.tw/blog/<br/>
軟體授權：Apache License, Version 2.0，請見license.txt

1.使用必要條件<br/>
　a.需有群益帳戶<br/>
　b.有開通報價API，並下載那API相關DLL檔<br/>
　c.利用那 API.xls 去接收報價，並透過 OSWINSCK.dll 以TCP Socket傳送。

2.使用方法<br/>
　a.主要接受TCP Socket程式為SocketServer.java。<br/>
　b.策略邏輯為NewDdeClient.java。<br/>
　c.目前設定需配合Dropbox使用，亦可自行修改不使用。<br/>
　d.GetWednesday.java是檢查每個月台指期和摩台期結算日。<br/>
　e.請自行設定排程時間，於每日早上八點四十五分之前執行。<br/>

3.下單機<br/>
　a.建議使用下單大師，http://moneyprinter.pixnet.net/blog<br/>
　b.或者，請參閱<a href="http://server.everfine.com.tw/blog/archives/2013/03/4.html">程式交易經驗分享系列(4) - 下單機設定及系列回顧</a><br/>

歡迎大家加入討論程式交易，http://www.facebook.com/tradingbot<br/>
若需要支援服務，還請小額贊助，支持永續發展此TradingBot。感謝！<br/>
聯絡資訊：philipzheng@gmail.com
