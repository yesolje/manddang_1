/**-----------------------------------------------------------------------------------------
 * Description : join.js
 **----------------------------------------------------------------------------------------*/

/********************************************************************************
 * Global Variable : 전역변수 정의
 ********************************************************************************/

/********************************************************************************
 * Document Ready
 ********************************************************************************/

document.addEventListener("DOMContentLoaded", function(){
    join.initEvent();

});

var join={
    initEvent:function(){
        document.getElementById('save').addEventListener("click", function(event){
          var jsonData = JSON.stringify({
                      userId: document.getElementById('userId').value,
                      userPwd: document.getElementById('userPwd').value
          });
          httpRequest = new XMLHttpRequest();
          httpRequest.onreadystatechange = () => {
            if (httpRequest.readyState === XMLHttpRequest.DONE) {
                  if (httpRequest.status === 200 || httpRequest.status === 201) {
                    alert('회원가입에 성공했습니다');
                    location.href = '/main';
                  } else {
                    alert('에러가 발생했습니다. 관리자에게 문의하세요');
                    console.log("code:"+httpRequest.status+"\n"+"message:"+httpRequest.responseText+"\n");
                  }
            }
          };
          httpRequest.open('POST', '/user/join', true);
          httpRequest.setRequestHeader('Content-Type', 'application/json;charset=utf-8');
          httpRequest.send(jsonData);
        });
    },



}