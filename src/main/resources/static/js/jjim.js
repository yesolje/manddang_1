/**-----------------------------------------------------------------------------------------
 * Description : jjim.js
 **----------------------------------------------------------------------------------------*/
/********************************************************************************
 * Global Variable : 전역변수 정의
 ********************************************************************************/

 //사용자가 찜한 주유소의 정보
 var oilPriceData;
 var table;

 //카카오맵,마커,오버레이 관련 객체
 var markerImage = new kakao.maps.MarkerImage('/images/gas-station_green.png', new kakao.maps.Size(69, 69), {offset: new kakao.maps.Point(27, 69)});
 var map;
 var marker;
 //객체_주유소 상세정보
 var stationDetailInfo;

/********************************************************************************
 * Document Ready
 ********************************************************************************/

document.addEventListener("DOMContentLoaded", async function(){
    jjim.initHeaderLoad();


    oilPriceData = await common.getStationDetailInfosByUserId();
    console.log(oilPriceData);
    oilPriceData = jjim.addOilPriceInStationDetailInfo(oilPriceData);
    jjim.initMapLoad();
    jjim.initMapMarkerLoad();
    jjim.initTabulatorLoad();
    jjim.initEvent();
});

var jjim={
    initHeaderLoad : function(){
        common.headerLoad();
        common.navLoad();
    },

    initEvent:function(){
        table.on("rowClick", async function(e, row){

        });
    },

    initMapLoad:function(){
        let container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
        let options = { //지도를 생성할 때 필요한 기본 옵션
        	center: new kakao.maps.LatLng(37.413293, 127.143591), //지도의 중심좌표.
        	level: 3 //지도의 레벨(확대, 축소 정도)
        };

        map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴
    },
    initTabulatorLoad:function(){
        table = new Tabulator("#listTabulator", {
         	data:oilPriceData,
         	layout:"fitColumns",
         	pagination:true,
            paginationSize:20,
         	columns:[
        	 	{title:"주유소ID", field:"UNI_ID",               visible:false},
        	 	{title:"주유소명",  field:"OS_NM",               tooltip:true, width: "30%"},
        	 	{title:"브랜드",   field:"POLL_DIV_CO",          tooltip:true, width: "15%"},
        	 	{title:"주소",     field:"NEW_ADR",              tooltip:true,width: "40%"},
                {title:"구주소",   field:"VAN_ADR",               visible:false},
                {title:"위도",   field:"latitude",               visible:false},
                {title:"경도",   field:"longitude",               visible:false},
                {title:"관리",     formatter:jjim.buttonFormatter,  width: "15%"},
         	],
        });
    },

    initMapMarkerLoad:function(){
        var pointArr = [];
        for(var i=0;i<oilPriceData.length;i++){
            var point={
                latlng : new kakao.maps.LatLng(oilPriceData[i].latitude,oilPriceData[i].longitude),
                content :
                `
                   <div class ="wrap">
                       <div class="title">
                           <div class="title-name">${oilPriceData[i].OS_NM}</div>

                       </div>
                       <div class="contents">
                           <input type="hidden" id="stationUniId" value=${oilPriceData[i].UNI_ID}>
                           <ul class="info-ul">
                               <li class="info-li">주소 : ${oilPriceData[i].NEW_ADR}</li>
                               <li class="info-li">브랜드 : ${oilPriceData[i].POLL_DIV_CO}</li>
                               <li class="info-li">전화번호 : ${oilPriceData[i].TEL}</li>
                               <li class="info-li">세차장 : ${oilPriceData[i].CAR_WASH_YN}</li>
                               <li class="info-li">휘발유 : ${oilPriceData[i].B027}</li>
                               <li class="info-li">경유 : ${oilPriceData[i].D047}</li>
                               <li class="info-li">고급휘발유 : ${oilPriceData[i].B034}</li>
                           </ul>
                           <div class="manage-buttons">
                               <button class="manage-button link-primary" onclick="main.alertPrice()">가격알림</button>
                           </div>
                       </div>
                  </div>
                `
            }
            pointArr.push(point);

        }
        var bounds = new kakao.maps.LatLngBounds();
        for(var i =0;i<pointArr.length;i++){
            marker = new kakao.maps.Marker({
                position : pointArr[i].latlng,
                map : map,
                image:markerImage,
                clickable : true
            });
            var infowindow = new kakao.maps.InfoWindow({
              content: pointArr[i].content, // 인포윈도우에 표시할 내용
              removable : true //x자가 안예쁨...
            });

            kakao.maps.event.addListener(marker, 'click', jjim.makeOverListener(map, marker, infowindow));
            //kakao.maps.event.addListener(marker, 'mouseout', jjim.makeOutListener(infowindow));



            //marker.setMap(map);
            bounds.extend(pointArr[i].latlng);
        }
        map.setBounds(bounds);
    },

    //list tabulator row 관리 버튼 함수
    buttonFormatter:function(cell, formatterParams, onRendered){
        return '<button class="btn btn-danger" onclick="jjim.handleButtonClick(\'' + cell.getData().UNI_ID+ '\')">삭제</button>';
    },

    //TODO : 아직 삭제 안됨
    handleButtonClick:function(uni_id){
        alert(uni_id + "를 정말 삭제하시겠습니까?");
    },

    //인포윈도우 켜짐
    makeOverListener: function(map, marker, infowindow) {
        return function() {
            infowindow.open(map, marker);
            infowindow.Xh.parentElement.querySelector("img").src='../images/close.png';
        };
    },

    // 인포윈도우를 닫는 클로저를 만드는 함수입니다
    makeOutListener: function(infowindow) {
        return function() {
            infowindow.close();
        };
    },

    addOilPriceInStationDetailInfo:function(oilPriceData){

        for(var i=0;i<oilPriceData.length;i++){
            Object.assign(oilPriceData[i],{B027:'',D047:'',B034:''});
            for(var j=0;j<oilPriceData[i].OIL_PRICE.length;j++){
                if(oilPriceData[i].OIL_PRICE[j].PRODCD =='B027'){
                    oilPriceData[i].B027 = oilPriceData[i].OIL_PRICE[j].PRICE;
                }else if(oilPriceData[i].OIL_PRICE[j].PRODCD =='D047'){
                    oilPriceData[i].D047 = oilPriceData[i].OIL_PRICE[j].PRICE;
                }else if(oilPriceData[i].OIL_PRICE[j].PRODCD =='B034'){
                    oilPriceData[i].B034 = oilPriceData[i].OIL_PRICE[j].PRICE;
                }
            }
        }
        return oilPriceData;
    },


}

