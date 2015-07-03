function fileList = getImgFiles(dirName)

  dirData = dir(dirName);      %# Get the data for the current directory
  dirIndex = [dirData.isdir];  %# Find the index for directories
  fileList = {dirData(~dirIndex).name}';  %'# Get a list of the files
  if ~isempty(fileList)                   
    % Filter out all non-images
    fileListPNG = fileList(cellfun(@(x) size(x,1)>0, strfind(fileList, '.png')));
    fileListJSON = fileList(cellfun(@(x) size(x,1)>0, strfind(fileList, '.json')));
    
    % Filter out images already with generated json
    fileListJSONOnlyName = cellfun(@(x) x(1:end-5), fileListJSON, 'UniformOutput', false);
    fileList = fileListPNG(cellfun(@(x) size(find(ismember(fileListJSONOnlyName, x(1:end-4))), 1)==0, fileListPNG));
    
    fileList = cellfun(@(x) fullfile(dirName,x),...  %# Prepend path to files
                       fileList,'UniformOutput',false);
  end
end