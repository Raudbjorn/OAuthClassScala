var webpack = require('webpack');

module.exports = require('./scalajs.webpack.config')

//setting the target to 'node' gets rid of lots of webpack errors
module.exports.target = 'node'
module.exports.plugins = (module.exports.plugins || []);