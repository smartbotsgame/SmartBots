Type SelMesh
	Field mesh
	Field name$
End Type

Type SelCamera
	Field camera
	Field x1#, y1#, z1#
	Field x2#, y2#, z2#
	Field state
End Type

Function UpdateCameras ( )
	For s.SelCamera = Each SelCamera
		If s\state = 1
			TranslateEntity s\camera, (s\x1 - EntityX(s\camera))/5, (s\y1 - EntityY(s\camera))/5, (s\z1 - EntityZ(s\camera))/5
		Else
			TranslateEntity s\camera, (s\x2 - EntityX(s\camera))/5, (s\y2 - EntityY(s\camera))/5, (s\z2 - EntityZ(s\camera))/5
		End If
	Next
End Function

Function CreateSelCamera.SelCamera ( x, y, w, h, r, g, b )
	s.SelCamera = New SelCamera
	s\camera = CreateCamera ( )
	CameraViewport s\camera, x, y, w, h
	CameraZoom s\camera, 2
	CameraClsColor s\camera, r, g, b
	PositionEntity s\camera, 0, 10, -30
	AlignToVector s\camera, -EntityX(s\camera), -EntityY(s\camera), -EntityZ(s\camera), 3
	s\x1 = EntityX(s\camera)
	s\y1 = EntityY(s\camera)
	s\z1 = EntityZ(s\camera)
	MoveEntity s\camera, 0, 0, -10
	s\x2 = EntityX(s\camera)
	s\y2 = EntityY(s\camera)
	s\z2 = EntityZ(s\camera)
	s\state = 0
	Return s
End Function

Function LoadMeshes ( dirm$ )
dir=ReadDir(dirm$)
Repeat
	File$=NextFile$(dir)
	If File$="" Exit
	If File$<>"." And File$<>".."
		If Right(File$,4)=".b3d"
			s.SelMesh = New SelMesh
			s\mesh = LoadMesh(dirm$+File$)
			s\name$ = Left ( File$,Len ( File$ ) - 4 )
			FitMesh s\mesh, -10, -10, -10, 20, 20, 20, 1
			HideEntity s\mesh
		EndIf
	EndIf
Forever
CloseDir dir
End Function

Function ShowMesh ( m.SelMesh )
	For s.SelMesh = Each SelMesh
		HideEntity s\mesh
	Next
	If m<>Null ShowEntity m\mesh
End Function

Function ShowCamera ( c.SelCamera )
	For s.SelCamera = Each SelCamera
		HideEntity s\camera
	Next
	If c<>Null ShowEntity c\camera
End Function

Function FreeMeshes ( )
	For s.SelMesh = Each SelMesh
		FreeEntity s\mesh
		Delete s
	Next
End Function

Function FreeCameras ( )
	For c.SelCamera = Each SelCamera
		FreeEntity c\camera
		Delete c
	Next
End Function