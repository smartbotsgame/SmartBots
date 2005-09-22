Type test
Field en
Field r
End Type

Dim timel(400)
Function createtest()


tes.test=New test
     tes\en=CreateSphere(16)
     FlipMesh tes\en
        ScaleEntity tes\en,2000,2000,2000
     Tex=LoadTexture("menu\M20.JPG")
     EntityTexture tes\en,Tex
       ScaleTexture Tex,.1,.1
       EntityType tes\en,2
       tes\r=1
  For i=1 To 6
     tes.test=New test
     tes\en=CreateSphere(32)
     PositionEntity tes\en,Rand(-2000,2000),Rand(0,100),Rand(-2000,2000)
     Texa=LoadTexture("menu\RoboSkin.jpg")
     EntityTexture tes\en,Texa
     sc=Rand(100,200)
     ScaleEntity tes\en,sc,sc,sc
     EntityType tes\en,2
     EntityPickMode tes\en,2
  Next
End Function 

;============================
Function render()
camt=CreateCamera()
CameraRange camt,.1,5000
MoveEntity camt,0,2500,0
RotateEntity camt,90,0,0
SetFont LoadFont("arial",30)
Collisions 10,2,2,2
createtest()
 While Not proh=400
  ;-------------
  ;синхронизация
	Repeat
		elapsed = MilliSecs () - Time
	Until elapsed	
	ticks = elapsed / period	
	tween# = Float (elapsed Mod period) / Float (period)	
For framelimit = 1 To ticks
	If framelimit = ticks CaptureWorld 
	Time = Time + period
   UpdateWorld
;=============
 ; TurnEntity camt,0,1,0



tm#=MilliSecs()
updatetest()
tm#=MilliSecs()-tm
timel(proh)=tm
proh=proh+1
;==============
Next
RenderWorld
   Color 255,255,255
  Text xcur-100,ycur,"TEST SYSTEM"
   Color 255,250,200
   Rect (xcur-201),ycur+99,402,12,0
   Color 255,50,0
   Rect (xcur-200),ycur+100,proh,10
Flip 0
Wend
test()
FreeEntity camt
ClearWorld()
proh=0

End Function

Function updatetest()
  For tes.test=Each test
     If tes\r=0
     TurnEntity tes\en,0,1,0
     PositionEntity tes\en,EntityX(tes\en),EntityY(tes\en)+Sin(Time/5)/3,EntityZ(tes\en)
     ent=EntityPick(tes\en,1500)
     If ent<>0
        EntityBlend tes\en,3
     Else
          EntityBlend tes\en,1
     EndIf
     EndIf
  Next
End Function

Function test()
  For i=0 To 399
     h#=h+timel(i)
   Next
  timelimit=h/400
 Return tm
End Function







