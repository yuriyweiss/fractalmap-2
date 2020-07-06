syms c;
R = vpasolve(c^16+8*c^15+28*c^14+60*c^13+94*c^12+116*c^11+114*c^10+94*c^9+69*c^8+44*c^7+26*c^6+14*c^5+5*c^4+2*c^3+c^2+c==0,c);
fileID = fopen('vpasolve_4.txt','w');
for i = 1 : length(R)
    fprintf(fileID,'re=%.20f;im=%.20f\n', real(R(i)), imag(R(i)));
end;
fclose(fileID);