function json = scanimg(I)
    BW = im2bw(I, 0.0001);
    results = ocr(BW, 'TextLayout', 'Block');
    regularExpr = '\d\d\d\d?';
    bboxes = locateText(results, regularExpr, 'UseRegexp', true);
    rooms = regexp(results.Text, regularExpr, 'match');
    json = '[ ';
    len = size(rooms);
    len = len(2);
    for n = 1:len
        bbox = bboxes(n,1:4);
        mid = (bbox(1:2)+bbox(3:4))/2;
        midx = mid(1);
        midy = mid(2);
        roomn = rooms{n};
        json = sprintf('%s{"mid":[%f,%f], "number":%s},', json, midx, midy, roomn);
    end
    json = json(1:length(json) - 1);
    json = sprintf('%s ]', json);
end