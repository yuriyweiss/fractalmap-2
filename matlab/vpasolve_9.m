syms c;
R = vpasolve(((((((((c^2+c)^2+c)^2+c)^2+c)^2+c)^2+c)^2+c)^2+c)^2+c==0,c);
fileID = fopen('vpasolve_9.txt','w');
for i = 1 : length(R)
    fprintf(fileID,'re=%.20f;im=%.20f\n', real(R(i)), imag(R(i)));
end;
fclose(fileID);