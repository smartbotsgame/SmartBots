Function GetSide ( x1#, y1#, x2#, y2#, x0#, y0# )
	Return Sgn((x1-x0)*(y2-y0) - (y1-y0)*(x2-x0))
End Function

Function GetSide0 ( x2#, y2#, x0#, y0# )
	Return Sgn(-x0*(y2-y0) + y0*(x2-x0))
End Function

Function InSect ( x0#, y0#, x1#, y1#, r#, angle#)

;PositionEntity luch,x1,60,y1
;PositionEntity luch2,x1,60,y1

	x0 = x0 - x1
	y0 = y0 - y1
	If x0*x0 + y0*y0 > r*r Then Return False
	r=r*2
	x1# = r*Cos(angle+20)
	y1# = r*Sin(angle+20)
	x2# = r*Cos(angle-20)
	y2# = r*Sin(angle-20)
	
	
	;PositionEntity pivL,x1,60,y1
	;PositionEntity pivL2,x2,60,y2
	;PointEntity luch,pivL
	;PointEntity luch2,pivL2
	
	
	If GetSide0(x1, y1, x0, y0)*GetSide0(x2, y2, x0, y0)>0 Return False
	
	If GetSide(x1, y1, x2, y2, x0, y0)*GetSide(x1, y1, 0, 0, x0, y0)>0 Return False
	
	Return True
End Function
;=============================
Function ScanRadar(angl#,t.tank)
RotateEntity t\pivot,0,angl,0
strukt=0 
struktAngle=0
frend=0
enemy=0 
bullet=0 
Rocketb=0 
Repair=0 
 
  ScanRadarT(t\corpus,t\xc,t\zc,1500,angl+90,t\Color$)
  ScanRadarB(t\corpus,t\xc,t\zc,1500,angl+90)
    MoveEntity t\pivot,0,0,-10
   ent=EntityPick (t\pivot,1600)
    MoveEntity t\pivot,0,0,10
   If ent<>0
    tp= TypePick(ent,t\corpus)
    If tp=3 
       strukt=Sqr((PickedX()-t\xc)*(PickedX()-t\xc)+(PickedZ()-t\zc)*(PickedZ()-t\zc))
          prm#=EntityYaw(t\pivot)-ASin(PickedNX#())
          struktAngle=(90-Abs(prm))*Sgn(prm) 
    EndIf
  EndIf

     SetVarValueInt(t\sc, 13, frend) ;frend%  ;13  
     SetVarValueInt(t\sc, 14, enemy) ;enemy%  ;14
     SetVarValueInt(t\sc, 15, Bullet) ;bulletBonus% ;15
     SetVarValueInt(t\sc, 16, Rocketb) ;rocketBonus% ;16
     SetVarValueInt(t\sc, 17, Repair) ;RepairBonus% ;17

     SetVarValue(t\sc, 18, strukt) ;strukt# ;18 
     SetVarValue(t\sc, 19, struktAngle) ;struktAngle# ;19 
End Function
;=============
Function ScanRadarT(ent,x#,z#,r#,a#,colort$)
    For t.tank=Each tank
    If ent<>t\corpus And t\live>0
     
    If InSect (t\xc,t\zc,x, z, r, a)
       If command=1
            If t\Color$=colort$
                   If frend=0
                 frend=t\corpus
                   Else
                      If EntityDistance (ent,frend)>EntityDistance (ent,t\corpus) frend=t\corpus
                    EndIf
             Else
                       If enemy=0
                 enemy=t\corpus
               Else
                If EntityDistance (ent,enemy)>EntityDistance (ent,t\corpus) enemy=t\corpus
               EndIf                  
             EndIf
         Else
           enemy=t\corpus
         EndIf
     EndIf  
   EndIf     
Next
End Function
;=================
Function ScanRadarB(ent,x#,z#,r#,a#)
    For b. bonus=Each bonus
    If b\Time=0 
    If InSect (EntityX(b\en),EntityZ(b\en),x, z, r, a)
       Select b\tip
        Case 1
            If Bullet=0
                Bullet=b\en
               Else
                 If EntityDistance (ent,Bullet)>EntityDistance (ent,b\en) Bullet=b\en
               EndIf      
        Case 2
           If Rocketb=0
                Rocketb=b\en
               Else
                 If EntityDistance (ent,Rocketb)>EntityDistance (ent,b\en) Rocketb=b\en
               EndIf 
        Case 3
          If Repair=0
                Repair=b\en
               Else
                 If EntityDistance (ent,Repair)>EntityDistance (ent,b\en) Repair=b\en
               EndIf
      End Select

     EndIf
  EndIf     
Next
End Function


;strukt 
;wall 
;frend
;enemy 
;bullet 
;Rocketb 
;Repair 
;==============================