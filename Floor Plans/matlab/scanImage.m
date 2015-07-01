function [bboxes, rooms] = scanImage(BW, I, showImage)
    regExpr = '\d\d\d+[ABC]?';
    results = ocr(BW, 'CharacterSet', '1234567890ABC', 'TextLayout', 'Block');
    bboxes = locateText(results, regExpr, 'UseRegexp', true);
    rooms = regexp(results.Text, regExpr, 'match');
    
    if showImage
        figure;
        Iname = insertObjectAnnotation(I, 'rectangle', bboxes, rooms);
        imshow(Iname);
    end
end