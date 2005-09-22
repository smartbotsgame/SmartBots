;================bot 1================
Function bot1(t.tank)

   t\move=.1
  t\angl=.4
   If t\target=0 t\target=findTarget(t\name$,t\target)
   If  t\target<>0
  t\a0= DeltaYaw#(t\turret,t\target)  
   If Abs(t\a0)<2         
       t\a1=DeltaPitch(t\stvol,t\target)-EntityDistance(t\turret,t\target)/50
       t\anglx=t\a1
  If Abs(t\a1)<2 
    Fire1(t.tank)                
  EndIf      
EndIf
t\angly=t\a0
EndIf


End Function
;=================bot2==============
Function bot2(t.tank)

If t\collignt<>0 Or t\collignP<>0
  ; t\move=-.1
  ; t\angl=-.2
Else
  t\move=t\move+.2
   t\angl=.3
EndIf


If t\target=0 t\target=findTarget(t\name$,t\target)

If  t\target<>0

If EntityVisible (t\turret,t\target)

  t\a0= DeltaYaw#(t\turret,t\target)  
   If Abs(t\a0)<2         
       t\a1=DeltaPitch(t\stvol,t\target)-EntityDistance(t\turret,t\target)/50
       t\anglx=t\a1
    If Abs(t\a1)<2 
       Fire1(t.tank)                
    EndIf      
    EndIf
     t\angly=t\a0
Else 
 t\target=findTarget(t\name$,t\target)
EndIf
EndIf

End Function
;==================


Function findTarget(n$,ent)
For t.tank=Each tank
If t\name$<>n$ And t\corpus<>ent And t\live>0
   Return t\corpus 
EndIf
Next
End Function