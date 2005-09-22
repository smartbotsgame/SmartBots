Type layer
	Field mesh
	Field surf
	Field ent
End Type
Type ent
	;vx vy and vz are offsets from x,y,z.
	Field name$
	Field numsurfs
	Field numverts%[10]
	;Field numtris%[10]
	
	Field tri             ;bank
	Field vx,vy,vz
	Field nx,ny,nz
	
	
	Field layer
	Field layersurf%[10]
	Field vertindex%[10]
	
	Field x#,y#,z#,pitch#,yaw#,roll#,sx#,sy#,sz#,r,g,b,a#,visible
	
End Type



Function SS_CreateLayer(alpha=0)
	l.layer=New layer
	l\mesh=CreateMesh()
	l\surf=CreateSurface(l\mesh)
	AddVertex l\surf,0,0,0
	VertexCoords l\surf,0,0,0,0
	ClearSurface l\surf	
	NameEntity l\mesh,Handle(l)
	l\ent=CreateBank()
	If alpha EntityFX l\mesh,34 Else EntityFX l\mesh,2
	Return l\mesh
End Function

Function SS_CreateEntity(entity,layer)
	;find layer
	l.layer=Object.layer(EntityName(layer))
	;create a virtual entity
	e.ent=New ent
	
	
	e\vx=CreateBank()
	e\vy=CreateBank()
	e\vz=CreateBank()	
	e\tri=CreateBank()
	e\nx=CreateBank()
	e\ny=CreateBank()
	e\nz=CreateBank()
	
	e\name$=Handle(e)
	e\layer=layer
	e\x=EntityX(entity)
	e\x=EntityY(entity)
	e\x=EntityZ(entity)
	

	
	e\r=255
	e\g=255
	e\b=255
	e\a#=1
	e\visible=1
	;create the mesh inside the layer & add the data to the virtual entity.
;-------------------------------------------------	
 msurfs=CountSurfaces(entity)
 For is=1 To msurfs
         
	surf=GetSurface(entity,is)                    ; surf object
	;update the virtual mesh
	e\numverts[is]=CountVertices(surf)-1            ;kol vertex y objekt
	 br=GetSurfaceBrush(surf) 
     layersurf=FindSurface(layer,br)
	 If layersurf=0  
	      layersurf=CreateSurface(layer,br) ;esli net to sozdat  surface
	  EndIf
    FreeBrush br 	
	e\layersurf[is]=layersurf                  ; surf new object	
	e\vertindex[is]=CountVertices(layersurf)   ;kol vertex y layer	
	;add data to layer + virtual mesh
	For i=0 To CountVertices(surf)-1
		AddVertex(layersurf,VertexX(surf,i),VertexY(surf,i),VertexZ(surf,i),VertexU(surf,i),VertexV(surf,i),VertexW(surf,i))
		VertexNormal layersurf,i+e\vertindex[is],VertexNX(surf,i),VertexNY(surf,i),VertexNZ(surf,i)
		WriteBankFloat(e\vx,VertexX(surf,i))
		WriteBankFloat(e\vy,VertexY(surf,i))
		WriteBankFloat(e\vz,VertexZ(surf,i))
		WriteBankFloat(e\nx,VertexNX(surf,i))
		WriteBankFloat(e\ny,VertexNY(surf,i))
		WriteBankFloat(e\nz,VertexNZ(surf,i))
	Next
		
	For it=0 To CountTriangles(surf)-1
		i2=e\vertindex[is];
		AddTriangle(layersurf,TriangleVertex(surf,it,0)+i2,TriangleVertex(surf,it,1)+i2,TriangleVertex(surf,it,2)+i2)
	Next
Next

e\numsurfs=msurfs
;--------------------
WriteBankInt(l\ent,Handle(e))	
Return Handle(e)
End Function

Function SS_PositionEntity(ent,x#,y#,z#)
	e.ent=Object.ent(ent)
	e\x=x : e\y=y : e\z=z
	
	RotateEntity ss_pivot,e\pitch,e\yaw,e\roll
	
	If e\visible=0 Return
	
   offset=0	
 For is=1 To e\numsurfs
   
      surfp=e\layersurf[is]
	For i=0 To e\numverts[is]
		TFormPoint ReadBankFloat(e\vx,offset),ReadBankFloat(e\vy,offset),ReadBankFloat(e\vz,offset),ss_pivot,0
		VertexCoords surfp,i+e\vertindex[is],e\x+TFormedX(),e\y+TFormedY(),e\z+TFormedZ()
		TFormNormal ReadBankFloat(e\nx,offset),ReadBankFloat(e\ny,offset),ReadBankFloat(e\nz,offset),ss_pivot,0
		VertexNormal surfp,i+e\vertindex[is],TFormedX(),TFormedY(),TFormedZ()
		offset=offset+4
	Next
 Next		
End Function

Function SS_TranslateEntity(ent,x#,y#,z#)
	e.ent=Object.ent(ent)
	e\x=e\x+x : e\y=e\y+y : e\z=e\z+z
	RotateEntity ss_pivot,e\pitch,e\yaw,e\roll
	If e\visible=0 Return
	offset=0
 For is=1 To e\numsurfs
	For i=0 To e\numverts[is]
		TFormPoint ReadBankFloat(e\vx,offset),ReadBankFloat(e\vy,offset),ReadBankFloat(e\vz,offset),ss_pivot,0
		VertexCoords e\layersurf[is],i+e\vertindex[is],e\x+TFormedX(),e\y+TFormedY(),e\z+TFormedZ()
		TFormNormal ReadBankFloat(e\nx,offset),ReadBankFloat(e\ny,offset),ReadBankFloat(e\nz,offset),ss_pivot,0
		VertexNormal e\layersurf[is],i+e\vertindex[is],TFormedX(),TFormedY(),TFormedZ()
		offset=offset+4
	Next
  Next	
End Function

Function SS_MoveEntity(ent,x#,y#,z#)
	e.ent=Object.ent(ent)
	RotateEntity ss_pivot,e\pitch,e\yaw,e\roll
	TFormVector x,y,z,ss_pivot,0
	e\x=e\x+TFormedX() : e\y=e\y+TFormedY() : e\z=e\z+TFormedZ()
	If e\visible=0 Return
	offset=0
	For is=1 To e\numsurfs
	For i=0 To e\numverts[is]
		TFormPoint ReadBankFloat(e\vx,offset),ReadBankFloat(e\vy,offset),ReadBankFloat(e\vz,offset),ss_pivot,0
		VertexCoords e\layersurf[is],i+e\vertindex[is],e\x+TFormedX(),e\y+TFormedY(),e\z+TFormedZ()
		TFormNormal ReadBankFloat(e\nx,offset),ReadBankFloat(e\ny,offset),ReadBankFloat(e\nz,offset),ss_pivot,0
		VertexNormal e\layersurf[is],i+e\vertindex[is],TFormedX(),TFormedY(),TFormedZ()		
		offset=offset+4
	Next
	Next
End Function

Function SS_TurnEntity(ent,pitch#,yaw#,roll#)
	e.ent=Object.ent(ent)
	RotateEntity ss_pivot,e\pitch#,e\yaw#,e\roll#
	TurnEntity ss_pivot,pitch#,yaw#,roll#
	e\pitch=EntityPitch(ss_pivot)
	e\yaw=EntityYaw(ss_pivot)
	e\roll=EntityRoll(ss_pivot)
	If e\visible=0 Return
	offset=0
	For is=1 To e\numsurfs
	For i=0 To e\numverts[is]
		TFormPoint ReadBankFloat(e\vx,offset),ReadBankFloat(e\vy,offset),ReadBankFloat(e\vz,offset),ss_pivot,0
		VertexCoords e\layersurf[is],i+e\vertindex[is],e\x+TFormedX(),e\y+TFormedY(),e\z+TFormedZ()		
		TFormNormal ReadBankFloat(e\nx,offset),ReadBankFloat(e\ny,offset),ReadBankFloat(e\nz,offset),ss_pivot,0
		VertexNormal e\layersurf[is],i+e\vertindex[is],TFormedX(),TFormedY(),TFormedZ()		
		offset=offset+4
	Next
	Next	
End Function

Function SS_RotateEntity(ent,pitch#,yaw#,roll#)
	e.ent=Object.ent(ent)
	RotateEntity ss_pivot,pitch#,yaw#,roll#
	e\pitch=EntityPitch(ss_pivot)
	e\yaw=EntityYaw(ss_pivot)
	e\roll=EntityRoll(ss_pivot)
	If e\visible=0 Return
	offset=0
	For is=1 To e\numsurfs
	For i=0 To e\numverts[is]
		TFormPoint ReadBankFloat(e\vx,offset),ReadBankFloat(e\vy,offset),ReadBankFloat(e\vz,offset),ss_pivot,0
		VertexCoords e\layersurf[is],i+e\vertindex[is],e\x+TFormedX(),e\y+TFormedY(),e\z+TFormedZ()		
		TFormNormal ReadBankFloat(e\nx,offset),ReadBankFloat(e\ny,offset),ReadBankFloat(e\nz,offset),ss_pivot,0
		VertexNormal e\layersurf[is],i+e\vertindex[is],TFormedX(),TFormedY(),TFormedZ()		
		offset=offset+4
	Next
	Next	
End Function

Function SS_TurnEntity2(ent,pitch#,yaw#,roll#)
	e.ent=Object.ent(ent)
	RotateEntity ss_pivot,e\pitch#,e\yaw#,e\roll#
	TurnEntity ss_pivot,pitch#,yaw#,roll#
	e\pitch=EntityPitch(ss_pivot)
	e\yaw=EntityYaw(ss_pivot)
	e\roll=EntityRoll(ss_pivot)
	If e\visible=0 Return
	offset=0
	For is=1 To e\numsurfs
	For i=0 To e\numverts[is]
		TFormPoint ReadBankFloat(e\vx,offset),ReadBankFloat(e\vy,offset),ReadBankFloat(e\vz,offset),ss_pivot,0
		VertexCoords e\layersurf[is],i+e\vertindex[is],e\x+TFormedX(),e\y+TFormedY(),e\z+TFormedZ()		
		TFormNormal ReadBankFloat(e\nx,offset),ReadBankFloat(e\ny,offset),ReadBankFloat(e\nz,offset),ss_pivot,0
		VertexNormal e\layersurf[is],i+e\vertindex[is],TFormedX(),TFormedY(),TFormedZ()		
		offset=offset+4
	Next
	Next	
End Function

Function SS_EntityAlpha(ent,a#)
	e.ent=Object.ent(ent)
	e\a#=a#	
	If e\visible=0 Return
	For is=1 To e\numsurfs
	For i=0 To e\numverts[is]
		VertexColor e\layersurf[is],i+e\vertindex[is],e\r,e\g,e\b;,e\a#
	Next
	Next	
End Function

Function SS_EntityColor(ent,r,g,b)
	e.ent=Object.ent(ent)
	e\r=r : e\g=g : e\b=b
	If e\visible=0 Return
	For is=1 To e\numsurfs
	For i=0 To e\numverts[is]
		VertexColor e\layersurf[is],i+e\vertindex[is],e\r,e\g,e\b;,e\a#
	Next
	Next	
End Function

Function SS_HideEntity(ent)
	e.ent=Object.ent(ent)
	If e\visible=0 Return
	For is=1 To e\numsurfs
	
	For i=0 To e\numverts[is]
	
		VertexCoords e\layersurf[is],i+e\vertindex[is],ss_large,ss_large,ss_large
		
	Next
	
	Next	
End Function

Function SS_ShowEntity(ent)
	e.ent=Object.ent(ent)
	e\visible=1
	RotateEntity ss_pivot,e\pitch,e\yaw,e\roll
	offset=0
	For is=1 To e\numsurfs
	For i=0 To e\numverts[is]
		TFormPoint ReadBankFloat(e\vx,offset),ReadBankFloat(e\vy,offset),ReadBankFloat(e\vz,offset),ss_pivot,0
		VertexCoords e\layersurf[is],i+e\vertindex[is],e\x+TFormedX(),e\y+TFormedY(),e\z+TFormedZ()
		TFormNormal ReadBankFloat(e\nx,offset),ReadBankFloat(e\ny,offset),ReadBankFloat(e\nz,offset),ss_pivot,0
		VertexNormal e\layersurf[is],i+e\vertindex[is],TFormedX(),TFormedY(),TFormedZ()
		VertexColor e\layersurf[is],i+e\vertindex[is],e\r,e\g,e\b;,e\a#
		offset=offset+4
	Next
	Next
End Function

Function SS_EntityX#(ent)
	e.ent=Object.ent(ent)
	Return e\x
End Function

Function SS_EntityY#(ent)
	e.ent=Object.ent(ent)
	Return e\x
End Function

Function SS_EntityZ#(ent)
	e.ent=Object.ent(ent)
	Return e\x
End Function

Function SS_EntityPitch#(ent)
	e.ent=Object.ent(ent)
	Return e\pitch
End Function

Function SS_EntityYaw#(ent)
	e.ent=Object.ent(ent)
	Return e\yaw
End Function

Function SS_EntityRoll#(ent)
	e.ent=Object.ent(ent)
	Return e\roll
End Function

Function WriteBankFloat(bank,value#)
	size=BankSize(bank)
	ResizeBank(bank,size+4)
	PokeFloat bank,size,value#
End Function

Function WriteBankInt(bank,value)
	size=BankSize(bank)
	ResizeBank(bank,size+4)
	PokeInt bank,size,value
End Function

Function ReadBankFloat#(bank,offset)
	Return PeekFloat(bank,offset)
End Function

Function ReadBankInt(bank,offset)
	Return PeekInt(bank,offset)
End Function
