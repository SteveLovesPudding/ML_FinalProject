%Testing validity of  robot arm move()
% ML final project, Tae Soo Kim, Steven Lin



fid1 = fopen('./data_oneArm_100_positions.txt','rt');

D1 = textscan(fid1, '%f %f %f', 'CollectOutput', true);

fclose(fid1);
data1 = D1{1};
%data2 = D2{1};

figure
hold on
for i=1:size(data1,1)
    scatter3(data1(i,1), data1(i,2), data1(i,3));
end



