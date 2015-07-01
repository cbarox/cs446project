function BW2 = firstPass(BW)
    regExpr = '[\dOoIiABC]+';
    results = ocr(BW, 'TextLayout', 'Block');
    bboxes = locateText(results, regExpr, 'UseRegexp', true);
    % texts = regexp(results.Text, regExpr, 'match');
    
    mask = false(size(BW));
    for i=1:size(bboxes,1)
        box = bboxes(i,:);
        for x=box(1):box(1)+box(3)-1
            for y=box(2):box(2)+box(4)-1
                mask(y,x) = true;
            end
        end
    end
    
    BW2 = ~logical(~BW.*mask);
    
    %figure;
    %Inew = insertObjectAnnotation(I, 'rectangle', bboxes, texts);
    %imshow(Inew);
end