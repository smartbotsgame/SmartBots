Const MaxFunc%=3000
Dim func$(MaxFunc)
For k = 0 To MaxFunc : func(k)="unknow command!!!" : Next
LoadCfg()
folder$="scripts/"
dir=ReadDir(folder)
File$=NextFile(dir)
While File<>""
If Right(File, 3)=".ai" Listing(folder+File)
File = NextFile(dir)
Wend
CloseDir(dir)
;WaitKey()
End

Function LoadCfg()
fname$="cfg/cfg.cfg"
If FileType(fname)<>1 Return 0
f=ReadFile(fname)
out=WriteFile("list.txt")
ReadLine(f)
While Not Eof(f)
s$ = ReadLine(f)+" "
p% = Instr( s$, ";")
If p>0 s = Left(s, p-1 )
If Trim(s)<>""
p0=Instr(s, " ")
name$ = Left(s, p0-1)
While Mid(s, p0, 1)=" " : p0=p0+1 : Wend
p=Instr(s, " ", p0)
num% = Int(Mid(s, p0, p-p0))
func(num)=Lower(name)
WriteLine out, Str(num%)+" "+func(num)
EndIf
Wend
CloseFile(f)
CloseFile(out)
Return 1
End Function
Function ToLen$(value, l)
Return String("0", l-Len(Str(value)))+Str(value)
End Function
Function Listing(File$)
Local vars$[100]
in=ReadFile ( File )
out=WriteFile ( File+".lst" )
WriteLine out, ReadLine(in) ; version
count% = ReadInt(in) ; count of variables
For k=1 To count
vars[k]=ReadLine(in)
WriteLine out, k+") "+vars[k]
Next
WriteLine out, "model = "+ReadLine(in)
WriteLine out, "name = "+ReadLine(in)
lst=ReadInt(in) Shr 2 - 1
WriteLine out, "------------------------------------"
WriteLine out, "no?iea [eia] iiaiiieea"
WriteLine out, "------------------------------------"
k%=0
While k<=lst
code%=ReadInt(in)
tr$=Upper(func(code))
name$="["+ToLen(k+1, 4)+"] ["+ToLen(code, 3)+"] "
Select Lower(tr)
Case "pop_"
k=k+1
var = ReadInt(in)
name=name+"pop var "+Upper ( vars[var] )
WriteLine out, name
Case "push_var"
k=k+1
var = ReadInt(in)
name=name+"push var "+Upper ( vars[var] )
WriteLine out, name
Case "goto_"
k=k+1
var = ReadInt(in)
name=name+"goto line N "+var
WriteLine out, name
Case "if"
k=k+1
var = ReadInt(in)
name=name+"If (false goto line N "+var+")"
WriteLine out, name
Case "push_num" :
k=k+1
num# = ReadFloat(in)
name=name+"push number "+num#
WriteLine out, name
Default :
WriteLine out, name+tr
End Select
k=k+1
Wend
.ending
WriteLine out, "------------------------------------"
CloseFile out
End Function
