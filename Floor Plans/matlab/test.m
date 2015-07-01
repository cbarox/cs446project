I = imread('in2.png');
level = graythresh(I);

% Upright form
BW = im2bw(I, level);
BW2 = clearLines(BW, 4, 40);
BW3 = firstPass(BW2);
[bboxes, rooms] = scanImage(BW3, I, false);

% Sideways form
BW2_ = rot90(BW2, -1);
BW3_ = firstPass(BW2_);
[bboxes_, rooms_] = scanImage(BW3_, I, false);
bboxes2_ = rotateBounds(BW3_, bboxes_);

bboxes_total=[bboxes;bboxes2_];
rooms_total=[rooms,rooms_];

Iname = insertObjectAnnotation(I, 'rectangle', bboxes_total, rooms_total);
figure;
imshow(Iname);