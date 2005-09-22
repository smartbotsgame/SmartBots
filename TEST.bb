Include "scripts.bb"
;=====================
Global TypeTank=1, TypeObstacle=2, TypePol=3
Global camera
;=====================
Type TANK
	Field model

	Field collignt  ;коллижн с другим танком
	Field collignP  ;коллижн с препятствием

	Field angl#  ;команда на поворот танка
	Field move#  ;команда на движение
	Field z#     ;скорость движения
	Field angly# ;команда на поворот башни
	Field anglx# ;команда на поворот ствола 
	Field fire
	Field pivot
	
	Field sc.script ; это ссылка на шаблон скрипта <<-----------------------------

	Field target ;цель атаки (нужно определить)
	;-------------корпус-------------
	;позиция
	Field xc#
	Field yc#
	Field zc#
	;углы
	Field rxc#
	Field ryc#
	Field rzc#
	;-------------башня-----------
	;позиция
	Field xt#
	Field yt#
	Field zt#
	;углы
	Field rxt#
	Field ryt#
	Field rzt#
	;--------------ствол----------
	;позиция
	Field xs#
	Field ys#
	Field zs#
	;углы
	Field rxs#
	Field rys#
	Field rzs#
End Type
;=====================

;+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Function CreateTank(x#, y#, z#, name$)
	t.tank=New tank
	t\model=CreateCube()
	EntityType t\model, TypeTank
	PositionEntity t\model, x, y, z
	EntityColor t\model, 255, 0, 0
	initScript(t, name)
	Return Handle(t)
End Function

Function CreateObstacle(x#, y#, z#, w#, h#, d#)
	cube=CreateCube()
	EntityColor cube, Rand(150, 255), Rand(150, 255), Rand(150, 255)
	EntityType cube, TypeObstacle
	PositionEntity cube, x, y, z
	ScaleEntity cube, w, h, d
	Return cube
End Function

Function UpdateTanks()
	For t.tank = Each tank
		t\collignT=EntityCollided(t\model, TypeTank)
		t\collignP=EntityCollided(t\model, TypeObstacle)
		UpdateScript(t)
		TurnEntity t\model, 0, t\angl, 0
		MoveEntity t\model, 0, 0, t\move
	Next
End Function

Function Init()
	Graphics3D 800, 600
	SetBuffer BackBuffer()
	SetFont LoadFont("system")
	Collisions TypeTank, TypePol, 2, 3
	Collisions TypeTank, TypeObstacle, 2, 3
	l=CreateLight()
	plane=CreatePlane()
	EntityType plane, TypePol
	EntityColor plane, 150, 150, 350
	camera=CreateCamera()
	PositionEntity camera, 0, 70, 0
	PointEntity camera, plane
	;walls 1
	createObstacle(0, 0, 40, 40, 2, 1)
	createObstacle(0, 0, -40, 40, 2, 1)
	createObstacle(40, 0, 0, 1, 2, 40)
	createObstacle(-40, 0, 0, 1, 2, 40)
	;walls 2
	createObstacle(-10, 0, 0, 3, 2, 5)
	createObstacle(10, 0, 20, 6, 2, 5)
	createObstacle(20, 0, -20, 10, 2, 2)
End Function
;=================================
init()
LoadCfg()
CreateTank(0, 1.5, 0, "scripts\sample.txt")
CreateTank(5, 1.5, 2, "scripts\sample2.txt")
While Not KeyHit(1)
	Cls
	UpdateTanks()
	UpdateWorld
	RenderWorld
	y=20
	For t.tank=Each tank
		Text 10, y, ":> "+ErrorMsg(t\sc\ErrorID)+t\sc\ErrorTag+" <:"
		y=y+20
	Next
	Flip
Wend

ClearWorld
EndGraphics
End