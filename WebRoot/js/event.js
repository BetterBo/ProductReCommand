/**
 * Created by ���� on 2017/2/16.
 */
//����ͼƬ
var currImage=2;
var Imgsrc=["img/Advertisement/bsry.jpg","img/Advertisement/shortsleeve.jpg",
    "img/Advertisement/yrf.jpg","img/Advertisement/DanBaiFen.jpg",
    "img/Advertisement/LG_4KTV.jpg","img/Advertisement/fss.jpg",
    "img/Advertisement/IPhone6s.jpg"
];
var Imgjson=["json/hs.json","json/shortsleeve.json",
    "json/yrf.json","json/DanBaiFen.json",
    "json/LG_4KTV.json","json/fss.json",
    "json/IPhone6s.json"
];
var Imgname=["����","ǳ��ɫ����","���޷�","���׷�","LG���ӻ�","��ɹ˪","IPhone6s"];

//Ԥ�Ȼ���ͼƬ
var Img=new Array(7);
for(var i=0;i<7;i++)
{
    Img[i]=new Image(290,290);
    Img[i].src=Imgsrc[i];
}


var imglrshow=false;


    function Pre()
    {
        currImage--;
        if(currImage<0) currImage=6;
        //ע���Ƿ�ɹ�
        console.log(currImage);
    }
function Next()
{
    currImage++;
    currImage%=7;
    //ע���Ƿ�ɹ�
    console.log(currImage);
}
function  ClearChild(parent)
{
    while(parent.hasChildNodes())
    {
        parent.removeChild(parent.firstChild);
    }
}


/*���غ�ͻ��Ч��*/
function show(id)
{
    $(id).show();
    $(id).addClass("FROM");
    setTimeout(function(){$(id).addClass("TO");},300);
}
function hide(id)
{
    $(id).removeClass("TO");
}
function  Refle()
{
    hide("#imgLeft");
    hide("#imgRight");
    hide("#imgReturn");
    imglrshow=false;
    setTimeout(function () {
        location.reload();
    },400);
}




function Change(dataroot)
{
    if(!imglrshow)
    {
        imglrshow=true;
        show("#imgLeft");
        show("#imgRight");
        show("#imgReturn");
    }
    $.ajax
    ({
        type: "GET",
        dataType: "json",
        url: dataroot,
        success: function (data)
        {
            //����沿ͼ
            var parent=document.getElementById("face");
            ClearChild(parent);
            parent.appendChild(Img[currImage]);

            //��������ͼ
            parent=document.getElementById('display_graphic');
            ClearChild(parent);

            var myChart = echarts.init(parent);

            option = {
                title: {
                    text: data[0].name+'�۸�����ͼ'
                },
                backgroundColor:"rgba(255,255,255,0.6)",
                tooltip: {
                    trigger: 'axis'
                },
                legend:
                {
                    data:data[3].plat
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                toolbox: {
                    feature: {
                        saveAsImage: {}
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: data[1].time
                },
                yAxis: {
                    type: 'value',
                    axisLabel :
                    {
                        formatter: '{value} Ԫ'
                    }
                },
                series:data[2].price
            };
            myChart.setOption(option);

            //����������Ϣ

            parent=document.getElementById("mytbody");
            var son=parent.getElementsByTagName("tr");


            for(var i=0;i<6;i++)
            {
                ClearChild(son[i]);
            }

            //��Ʒ����
            var td1=document.createElement("td");
            td1.innerHTML="��Ʒ����:  ";
            var td2=document.createElement("td");
            td2.innerHTML=data[0].name;
            son[0].appendChild(td1);
            son[0].appendChild(td2);

            //�۸�Χ
            var td3=document.createElement("td");
            td3.innerHTML="�۸�Χ:  ";
            var td4=document.createElement("td");
            td4.innerHTML=data[4].rangeprice;
            son[1].appendChild(td3);
            son[1].appendChild(td4);


            //�Ƽ�ƽ̨
            var td5=document.createElement("td");
            td5.innerHTML="�Ƽ�ƽ̨:  ";
            var td6=document.createElement("td");
            var str="";
            for(var i=0;i<data[3].plat.length;i++)
            {
                str+=data[3].plat[i]+" ";
            }
            td6.innerHTML=str;
            son[2].appendChild(td5);
            son[2].appendChild(td6);

        }
    });

}
