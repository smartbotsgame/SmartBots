Dim HelpMsg$ ( 0 )
Global ScriptVersion$

Type Word
	Field s$
	Field o$
	Field id%
	Field params%
	Field res%
	Field format$ ; формат параметров
End Type

;=========================
Function min#(a#, b#)
	If a<b Return a
	Return b
End Function

Function max#(a#, b#)
	If a>b Return a
	Return b
End Function

Function GetGroup(s$)
	c=Asc(s)
	If (c>=48) And (c<=57) Return 1;1..9
	If (c=46) Return 1; .
	If c=95 Return 1;_
	If (c>=60) And (c<=62) Return 2;< > =
	If (c>=65) And (c<=90) Return 1;A..Z
	If (c>=97) And (c<=122) Return 1;a..z
	If (c>=192) And (c<=255) Return 1;А..я
	If (c=35) Or (c=37) Return 1 ; # %
	If c=40 Return 10; (
	If c=41 Return 10; )
	If c=42 Return 11;*
	If c=43 Return 11;+
	If c=45 Return 11;-
	If c=47 Return 11;/
	If c = 124 Return 12 ; |
	Return 0
End Function

Function NextGroup(s$, pos)
	If pos>=Len(s) Return pos+1
	If (Mid(s, pos, 1)=")") Or (Mid(s, pos, 1)="(") Return pos+1
	curr=GetGroup(Mid(s, pos, 1))
	For k=pos+1 To Len(s)
		gr=GetGroup(Mid(s, k, 1))
		If (curr<>gr) Exit
	Next
	Return k
End Function

Function BackToFront$ ( s$ )
	res$=""
	For k = Len ( s ) To 1 Step -1
		res=res+Mid ( s, k, 1 )
	Next
	Return res
End Function

Function LoadCfg()
	name$="cfg\cfg.cfg"
	If FileType(name)<>1 Return
	f=ReadFile(name)
	ScriptVersion=ReadLine(f)
	Delete Each word
	While Not Eof(f)
		original$=Trim(ReadLine(f))+" "
		g=Instr(original, " ")
		s$=Trim(Left(original, g-1))
		If (Left(original, 1)<>";") And (s<>"")
			n.Word=New Word
			n\s=Lower(s)
			n\o=s
			g2=NextGroup(original, g)
			g3=NextGroup(original, g2)
			s$=Trim(Mid(original, g2, g3-g2))
			n\id=Int( s )
			g2=NextGroup(original, g3)
			g3=NextGroup(original, g2)
			s$=Trim(Mid(original, g2, g3-g2))
			n\params=Int( s )
			g2=NextGroup(original, g3)
			g3=NextGroup(original, g2)
			s$=Trim(Mid(original, g2, g3-g2))
			n\res=Int( s )
			g2=NextGroup(original, g3)
			g3=NextGroup(original, g2)
			n\format=BackToFront ( Trim(Mid(original, g2, g3-g2)) )
		End If
	Wend
	CloseFile f
	; read HelpMsg
	name$="cfg\help.cfg"
	If FileType(name)<>1 Return
	f=ReadFile(name)
	count% = Int ( ReadLine ( f ) )
	Dim HelpMsg ( count )
	For k = 1 To count
		s$ = ReadLine ( f )
		pos% = Instr ( s, " " )
		If pos=0 pos=Len(s)+1
		num% = Int ( Left ( s, pos-1 ) )
		s$ = ReadLine ( f )
		If num>=0 HelpMsg ( num ) = s
	Next
	CloseFile f
End Function

Function Text_ ( x, y, w, h, s$ )
	start = 1
	pos2 = start
	sx = x
	sy = y
	While pos2 <= Len ( s )
		pos2 = Instr ( s, "|", start )
		If pos2 = 0 pos2 = Len ( s ) + 1
		While start < pos2
			pos = NextGroup ( s, start )
			st$ = Mid ( s, start, pos - start )
			sw = StringWidth ( st )
			If sx + sw > x+w sx = x : sy = sy + FontHeight ( )
			If sy > y+h Exit
			For k = 1 To Len(st)
			Text sx+StringWidth(Left(st, k-1)), sy, Mid(st, k, 1)
			Next

			sx = sx + sw
			start = pos
		Wend
		start = pos2+1
		sx = x
		sy = sy + FontHeight ( )
	Wend
End Function

;Function ToLen$(n%, c%)
;	res$=Str(n)
;	res=String("0", c-Len(res))+res
;	Return res
;End Function