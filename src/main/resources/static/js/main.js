/**-----------------------------------------------------------------------------------------
 * Description : main.js
 **----------------------------------------------------------------------------------------*/
/********************************************************************************
 * Global Variable : 전역변수 정의
 ********************************************************************************/
 var sampledata = [ //자료 불러오는 대로 삭제할 예정
 	{id:1, name:"AAA주유소", location:"경기도 성남시 분당구 동판교로 212", priceGasoline:"1780", priceDiesel:"1230"},
 	{id:2, name:"BBB주유소", location:"경기도 성남시 분당구 동판교로 212", priceGasoline:"1780", priceDiesel:"1230"},
 	{id:3, name:"CCC주유소", location:"경기도 성남시 분당구 동판교로 212", priceGasoline:"1780", priceDiesel:"1230"},
 	{id:4, name:"DDD주유소", location:"경기도 성남시 분당구 동판교로 212", priceGasoline:"1780", priceDiesel:"1230"},
 	{id:5, name:"EEE주유소", location:"경기도 성남시 분당구 동판교로 212", priceGasoline:"1780", priceDiesel:"1230"},
 ];
 //var AREA;
 var oilPriceData;
/********************************************************************************
 * Document Ready
 ********************************************************************************/

document.addEventListener("DOMContentLoaded", async function(){
    //AREA = await common.getAreaFetch();
    //console.log(AREA);

    //나중에 유종별로 검색할때 이거 참고
    //var selectedOil = document.querySelector('input[name="priceSort"]:checked').value;

    //화면 로드될때 유가 최초 로드
    oilPriceData = await common.getOilPriceFetch('','B027');
    main.initHeaderLoad();
    main.initEvent();
    main.initMapLoad();
    main.initTabulatorLoad();
    common.loadLocationSelectBox();
});

var main={
    initHeaderLoad : function(){
        common.headerLoad();
        common.navLoad();
    },
    initEvent:function(){
        //document.getElementById('login').addEventListener("click", function(event){
        //  document.getElementById('loginForm').submit();
        //});
        document.getElementById('searchSido').addEventListener("change", function(event){
            common.loadLocationSelectBox(document.getElementById('searchSido').value);
        });
        document.getElementById('searchGungu').addEventListener("change", function(event){
            var testData;
            //var selectedArea = document.querySelector('#searchGungu').value;
            //console.log(selectedArea);
            //testData = await common.getOilPriceFetch();
        });

    },
    initMapLoad:function(){
        let container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
        let options = { //지도를 생성할 때 필요한 기본 옵션
        	center: new kakao.maps.LatLng(33.450701, 126.570667), //지도의 중심좌표.
        	level: 3 //지도의 레벨(확대, 축소 정도)
        };

        let map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴
    },
    initTabulatorLoad:function(){
        var table = new Tabulator("#listTabulator", {
         	data:sampledata, //assign data to table
         	layout:"fitColumns", //fit columns to width of table (optional)
         	columns:[ //Define Table Columns
        	 	{title:"주유소명", field:"name", width: "20%"},
        	 	{title:"위치", field:"location"},
        	 	{title:"휘발유", field:"priceGasoline", width: "15%"},
        	 	{title:"경유", field:"priceDiesel", width: "15%"},
         	],
        });
    },


}

