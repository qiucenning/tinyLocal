--
--  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
--
--  Licensed under the GPL, Version 3.0 (the "License");
--  you may not use this file except in compliance with the License.
--  You may obtain a copy of the License at
--
--       http://www.gnu.org/licenses/gpl.html
--
--  Unless required by applicable law or agreed to in writing, software
--  distributed under the License is distributed on an "AS IS" BASIS,
--  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--  See the License for the specific language governing permissions and
--  limitations under the License.
--

select 25
, +6.34
, 0.5
, 25e-03
, -1 -- Here are some valid floating-point number literals:
, 25f
, +6.34F
, 0.5d
, -1D
, 1.
, .5
, (sysdate -1d)   -- here we substract "one" in decimal format
, sysdate -1m   -- here we substract "one" and "m" is column's alias
, sysdate -1dm
, 1.-+.5
, 1.+.5
, 1.+.5D
, 1.+.5DM
, 1.D
, 1.M
, .5M
, .5DM
from dual

